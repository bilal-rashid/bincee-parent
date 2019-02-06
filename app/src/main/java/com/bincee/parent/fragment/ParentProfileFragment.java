package com.bincee.parent.fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bincee.parent.MyApp;
import com.bincee.parent.R;
import com.bincee.parent.api.model.MyResponse;
import com.bincee.parent.api.model.ProfileResponse;
import com.bincee.parent.api.model.Student;
import com.bincee.parent.base.BFragment;
import com.bincee.parent.databinding.FragmentParentProfileBinding;
import com.bincee.parent.databinding.RegisteredKidRowBinding;
import com.bincee.parent.helper.ActivityResultHelper;
import com.bincee.parent.helper.ImageFilePath;
import com.bincee.parent.helper.PermissionHelper;
import com.bincee.parent.helper.StorageUtils;
import com.bincee.parent.observer.EndpointObserver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParentProfileFragment extends BFragment {


    FragmentParentProfileBinding binding;
    private VM myLivData;
    private PermissionHelper permissionHelper;
    private ActivityResultHelper activityResultHelper;

    public ParentProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(kidsAdapter);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_parent_profile, container, false);
        myLivData = ViewModelProviders.of(this).get(VM.class);
        binding.setModal(myLivData);
        binding.setHandlers(new MyListners() {
            @Override
            public void onProfileClicked(View view) {
                showImageSelection();
            }
        });

        myLivData.loading.observe(this, aBoolean -> {

            if (aBoolean) {
                binding.progressBar.setVisibility(View.VISIBLE);
            } else {
                binding.progressBar.setVisibility(View.GONE);

            }
        });
        myLivData.parentErrorListner.observe(this, throwableEvent -> {

            Throwable data = throwableEvent.getData();
            if (data != null) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setTitle("Error").setMessage(data.getMessage())
                        .setPositiveButton("OK", (dialog, which) -> {

                        }).show();

            }

        });
        myLivData.kidsLoading.observe(this, aBoolean -> {
            if (aBoolean) {
                binding.kidsProgressbar.setVisibility(View.VISIBLE);
            } else {
                binding.kidsProgressbar.setVisibility(View.GONE);

            }
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(myLivData.kidsAdapter);

        binding.setLifecycleOwner(this);


        return binding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionHelper != null) {
            permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);

        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (activityResultHelper != null) {
            activityResultHelper.onActivityResult(requestCode, resultCode, data);

        }
    }

    private static class Event<T> {
        public boolean isHandled = false;
        private T data;

        public Event(T data) {
            this.data = data;
        }

        public T getData() {
            if (!isHandled) {
                isHandled = true;
                return data;
            } else return null;

        }
    }

    private void showImageSelection() {
        new AlertDialog.Builder(getContext())
                .setTitle("Select Image From")
                .setItems(new String[]{"Gallery", "Camera"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                permissionHelper = new PermissionHelper();
                                permissionHelper.with(ParentProfileFragment.this).permissionId(52)
                                        .requiredPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE})
                                        .setListner(new PermissionHelper.PermissionCallback() {
                                            @Override
                                            public void onPermissionGranted() {
                                                Intent intent = new Intent();
                                                intent.setType("image/*");
                                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 25);

                                                activityResultHelper = new ActivityResultHelper();
                                                activityResultHelper.with(ParentProfileFragment.this)
                                                        .setRequestCode(25)
                                                        .OnResult(new ActivityResultHelper.Result() {
                                                            @Override
                                                            public void ResultOk(Intent data) {


                                                                String realPath = ImageFilePath.getPath(getContext(), data.getData());

                                                                Log.i(ParentProfileFragment.this.getClass().getSimpleName(), "onActivityResult: file path : " + realPath);
                                                                myLivData.updateProfilePic(realPath);


                                                            }

                                                            @Override
                                                            public void Failed() {

                                                            }
                                                        })
                                                        .start();

                                            }

                                            @Override
                                            public void onPermissionFailed() {

                                                MyApp.showToast("Read External Storage Permission Required");
                                            }
                                        }).request();

                                break;
                            case 1:
                                permissionHelper = new PermissionHelper();
                                permissionHelper.with(ParentProfileFragment.this)
                                        .permissionId(69)
                                        .requiredPermissions(new String[]{Manifest.permission.CAMERA})
                                        .setListner(new PermissionHelper.PermissionCallback() {
                                            @Override
                                            public void onPermissionGranted() {


                                                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                File imageFile = StorageUtils.offlineImageNewFile(getActivity(), UUID.randomUUID().toString());
                                                Uri uri = StorageUtils.fileToUri(getContext(), imageFile);
                                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                                                takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                                                    startActivityForResult(takePictureIntent, 66);


                                                    activityResultHelper = new ActivityResultHelper();
                                                    activityResultHelper.with(ParentProfileFragment.this)
                                                            .setRequestCode(66)
                                                            .OnResult(new ActivityResultHelper.Result() {
                                                                @Override
                                                                public void ResultOk(Intent data) {


                                                                    myLivData.updateProfilePic(imageFile.getAbsolutePath());
                                                                }

                                                                @Override
                                                                public void Failed() {

                                                                }
                                                            }).start();
                                                }
                                            }

                                            @Override
                                            public void onPermissionFailed() {
                                                MyApp.showToast("Read External Storage Permission Required");

                                            }
                                        })
                                        .request();

                                break;
                        }
                    }
                })
                .create().show();

    }

    private static class KidsAdapter extends RecyclerView.Adapter<VH> {
        public List<Student> data = new ArrayList<>();

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.registered_kid_row, parent, false);
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            RegisteredKidRowBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.registered_kid_row, parent, false);

            return new VH(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {

            holder.bind(data.get(holder.getAdapterPosition()));


        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    private static class VH extends RecyclerView.ViewHolder {
        private final RegisteredKidRowBinding binding;
        private Student student;

        public VH(@NonNull RegisteredKidRowBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }


        public void bind(Student student) {
            this.student = student;
            binding.setStudent(student);


        }
    }


    @Override
    public void onResume() {
        super.onResume();
        setActivityTitle("MY PROFILE");
    }


    public interface MyListners {
        void onProfileClicked(View view);

    }

    public static class VM extends ViewModel {

        public MutableLiveData<String> username = new MutableLiveData<>();
        public MutableLiveData<String> contact = new MutableLiveData<>();
        public MutableLiveData<String> address = new MutableLiveData<>();
        public MutableLiveData<String> photo = new MutableLiveData<>();

        public MutableLiveData<Boolean> loading = new MutableLiveData<>();
        public MutableLiveData<Boolean> kidsLoading = new MutableLiveData<>();
        public MutableLiveData<Event<Throwable>> parentErrorListner = new MutableLiveData<>();
        public MutableLiveData<List<Student>> students = new MutableLiveData<>();
        KidsAdapter kidsAdapter;
        CompositeDisposable compositeDisposable = new CompositeDisposable();


        public VM() {
            getParentData();
            getParentKidsData();
            initKidsAdapter();

        }

        private void initKidsAdapter() {
            kidsAdapter = new KidsAdapter();
        }

        @Override
        protected void onCleared() {
            super.onCleared();
            compositeDisposable.clear();
        }

        private void getParentData() {

            loading.setValue(true);
            compositeDisposable.add(MyApp.endPoints.getParent(MyApp.instance.user.id + "")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(disposable -> {
                        loading.setValue(true);

                    })
                    .subscribeWith(new EndpointObserver<MyResponse<ProfileResponse>>() {
                        @Override
                        public void onComplete() {
                            loading.setValue(false);

                        }

                        @Override
                        public void onData(MyResponse<ProfileResponse> response) throws Exception {

                            if (response.status == 200) {

                                username.setValue(response.data.fullname);
                                address.setValue(response.data.address);
                                contact.setValue(response.data.phoneNo);
                                photo.setValue(response.data.photo);

                            } else {
                                throw new Exception(response.data.message);
                            }

                        }

                        @Override
                        public void onHandledError(Throwable e) {
                            e.printStackTrace();

                            parentErrorListner.setValue(new Event<Throwable>(e));
                            loading.setValue(false);
                        }
                    })
            );
        }

        private void getParentKidsData() {

            compositeDisposable.add(MyApp.endPoints.parentsStudentsList(MyApp.instance.user.id + "")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(disposable -> {
                        kidsLoading.setValue(true);

                    })
                    .subscribeWith(new EndpointObserver<MyResponse<ArrayList<Student>>>() {
                        @Override
                        public void onComplete() {
                            kidsLoading.setValue(false);

                        }

                        @Override
                        public void onData(MyResponse<ArrayList<Student>> response) throws Exception {

                            if (response.status == 200) {


                                students.setValue(response.data);
                                kidsAdapter.data = students.getValue();
                                kidsAdapter.notifyDataSetChanged();
                            } else {
                                throw new Exception(response.status + "");
                            }

                        }

                        @Override
                        public void onHandledError(Throwable e) {
                            e.printStackTrace();

                            parentErrorListner.setValue(new Event<Throwable>(e));
                            kidsLoading.setValue(false);
                        }
                    })
            );
        }

        public void updateProfilePic(String realPath) {


            File file = new File(realPath);
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("image", file.getName(), requestFile);

            loading.setValue(true);

            EndpointObserver<MyResponse> observer = MyApp.endPoints.uploadImage(body)
                    .flatMap(uploadImageResponceResponse ->
                            MyApp.endPoints.updateProfile(MyApp.instance.user.id + "", "http://" + uploadImageResponceResponse.data.path)
                    )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new EndpointObserver<MyResponse>() {
                        @Override
                        public void onComplete() {
                            loading.setValue(false);
                        }

                        @Override
                        public void onData(MyResponse o) throws Exception {

                            if (o.status == 200) {

                                getParentData();
                            } else {

                                throw new Exception(o.status + "");
                            }
                        }

                        @Override
                        public void onHandledError(Throwable e) {
                            e.printStackTrace();
                            loading.setValue(false);
                            parentErrorListner.setValue(new Event<Throwable>(e));


                        }
                    });
            compositeDisposable.add(observer);


        }

    }

}
