package com.rowan.homepwner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class ItemFragment extends Fragment {

    private static final String ARG_ITEM_ID = "item_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_PHOTO= 2;

    private Item mItem;

    private File mPhotoFile;

    private EditText mNameField;
    private EditText mSerialField;
    private EditText mValueField;

    private Button mReportButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private Button mDateButton;

    private Callbacks mCallbacks;
    /**
     * Required interface for hosting activities
     */
    public interface Callbacks {
        void onItemUpdated(Item item);
    }

    public static ItemFragment newInstance(UUID itemID) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_ITEM_ID, itemID);
        ItemFragment fragment = new ItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID itemId = (UUID) getArguments().getSerializable(ARG_ITEM_ID);
        mItem = ItemLab.get(getActivity()).getItem(itemId);
        mPhotoFile = ItemLab.get(getActivity()).getPhotoFile(mItem);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_item, container, false);

        mNameField = v.findViewById(R.id.item_name);
        mNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
                // This space intentionally left blank
            }
            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                mItem.setName(s.toString());
                updateItem();
            }
            @Override
            public void afterTextChanged(Editable s) {
                // This one too
            }
        });

        mSerialField = v.findViewById(R.id.item_serial);
        mSerialField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
                // This space intentionally left blank
            }
            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                mItem.setSerial(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
                // This one too
            }
        });

        mDateButton = v.findViewById(R.id.item_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(mItem.getDate());
                dialog.setTargetFragment(ItemFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mValueField = v.findViewById(R.id.item_value);
        mValueField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
                // This space intentionally left blank
            }
            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                mItem.setValue(s.toString());
                updateItem();
            }
            @Override
            public void afterTextChanged(Editable s) {
                // This one too
            }
        });

        mReportButton = (Button) v.findViewById(R.id.item_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getItemReport());
                i.putExtra(Intent.EXTRA_SUBJECT,
                        getString(R.string.item_report_subject));
                i = Intent.createChooser(i, getString(R.string.send_report));
                startActivity(i);
            }
        });

        PackageManager packageManager = getActivity().getPackageManager();

        mPhotoButton = (ImageButton) v.findViewById(R.id.item_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = mPhotoFile != null &&
                captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);

        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = FileProvider.getUriForFile(getActivity(),
                        "com.rowan.homepwner.fileprovider",
                        mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                List<ResolveInfo> cameraActivities = getActivity()
                        .getPackageManager().queryIntentActivities(captureImage,
                                PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo activity : cameraActivities) {
                    getActivity().grantUriPermission(activity.activityInfo.packageName,
                            uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });
        mPhotoView = (ImageView) v.findViewById(R.id.item_photo);
        updatePhotoView();

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        else if (requestCode == REQUEST_DATE) {
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mItem.setDate(date);
            updateItem();
            updateDate();
        }

        else if (requestCode == REQUEST_PHOTO) {
            Uri uri = FileProvider.getUriForFile(getActivity(),
                    "com.rowan.homepwner.fileprovider",
                    mPhotoFile);
            getActivity().revokeUriPermission(uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            updateItem();
            updatePhotoView();
        }

    }

    private void updateItem() {
        ItemLab.get(getActivity()).updateItem(mItem);
        mCallbacks.onItemUpdated(mItem);
    }

    private void updateDate() {
        mDateButton.setText(mItem.getDate().toString());
    }

    private String getItemReport() {
        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat,
                mItem.getDate()).toString();
        String report = getString(R.string.item_report,
                mItem.getName(), dateString, mItem.getSerial(), mItem.getValue());
        return report;
    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }
}
