package com.findxain.uberparentapp.fragment;


import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.findxain.uberparentapp.MyApp;
import com.findxain.uberparentapp.R;
import com.findxain.uberparentapp.api.model.MyResponse;
import com.findxain.uberparentapp.api.model.ProfileResponse;
import com.findxain.uberparentapp.api.model.Student;
import com.findxain.uberparentapp.base.BFragment;
import com.findxain.uberparentapp.databinding.FragmentParentProfileBinding;
import com.findxain.uberparentapp.databinding.RegisteredKidRowBinding;
import com.findxain.uberparentapp.observer.EndpointObserver;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParentProfileFragment extends BFragment {


    FragmentParentProfileBinding bind;
    private VM myLivData;

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

        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_parent_profile, container, false);
        myLivData = ViewModelProviders.of(this).get(VM.class);
        bind.setModal(myLivData);
        myLivData.loading.observe(this, aBoolean -> {

            if (aBoolean) {
                bind.progressBar.setVisibility(View.VISIBLE);
            } else {
                bind.progressBar.setVisibility(View.GONE);

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
                bind.kidsProgressbar.setVisibility(View.VISIBLE);
            } else {
                bind.kidsProgressbar.setVisibility(View.GONE);

            }
        });

        bind.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        bind.recyclerView.setAdapter(myLivData.kidsAdapter);

        bind.setLifecycleOwner(this);


        return bind.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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


    public static class VM extends ViewModel {

        public MutableLiveData<String> username = new MutableLiveData<>();
        public MutableLiveData<String> contact = new MutableLiveData<>();
        public MutableLiveData<String> address = new MutableLiveData<>();
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

                            } else {
                                throw new Exception(response.data.message);
                            }

                        }

                        @Override
                        public void onHandledError(Throwable e) {

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

                            parentErrorListner.setValue(new Event<Throwable>(e));
                            kidsLoading.setValue(false);
                        }
                    })
            );
        }


    }

}
