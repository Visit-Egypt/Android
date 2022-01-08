package com.visitegypt.ui.account;

import static com.visitegypt.utils.Constants.SHARED_PREF_FIRST_NAME;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.data.repository.PostRepositoryImp;
import com.visitegypt.domain.model.Post;
import com.visitegypt.domain.model.PostPage;
import com.visitegypt.domain.model.converters.PostConverter;
import com.visitegypt.domain.usecase.GetPostUseCase;
import com.visitegypt.domain.usecase.GetPostsByUser;
import com.visitegypt.utils.Constants;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.functions.Consumer;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

@HiltViewModel
public class AccountViewModel extends ViewModel {
    SharedPreferences sharedPreferences;
    MutableLiveData<List<Post>> mutableLiveDataMyPosts = new MutableLiveData<>();
    MutableLiveData<String> mutableLiveDataName = new MutableLiveData<>();
     GetPostsByUser getPostsByUser;
    @Inject
    public AccountViewModel(SharedPreferences sharedPreferences, GetPostsByUser getPostsByUser) {
        this.sharedPreferences = sharedPreferences;
        this.getPostsByUser = getPostsByUser;
    }

    public void getUserInformation() {
        mutableLiveDataName.setValue(sharedPreferences.getString(Constants.SHARED_PREF_FIRST_NAME, null));
        //
        getUserPosts();

    }

    private void getUserPosts() {
        //when backend finishes there work start to implement
        Log.d("TAG", "accept List of posts:  welcome");
        getPostsByUser.execute(new Consumer<PostPage>() {
                                   @Override
                                   public void accept(PostPage postPage) throws Throwable {
                                       mutableLiveDataMyPosts.setValue(postPage.getPosts());
                                   }
                               }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                try {
                    ResponseBody body = ((HttpException) throwable).response().errorBody();
                    JSONObject jObjectError = new JSONObject(body.string());
                    Log.d("TAG", "accept try : " + jObjectError.getJSONArray("errors").toString());
                    if (jObjectError.getJSONArray("errors").toString().contains("msg")) {


                    } else {

                    }
                } catch (Exception e) {
                    Log.d("TAG", "accept catch: " + e.toString());
                }
            }
                               }

        );
    }

}
