// Generated by Dagger (https://dagger.dev).
package com.visitegypt.domain.usecase;

import android.content.SharedPreferences;
import com.visitegypt.domain.repository.UserRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import javax.inject.Provider;

@DaggerGenerated
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class UploadUserPhotoUseCase_Factory implements Factory<UploadUserPhotoUseCase> {
  private final Provider<UserRepository> userRepositoryProvider;

  private final Provider<SharedPreferences> sharedPreferencesProvider;

  public UploadUserPhotoUseCase_Factory(Provider<UserRepository> userRepositoryProvider,
      Provider<SharedPreferences> sharedPreferencesProvider) {
    this.userRepositoryProvider = userRepositoryProvider;
    this.sharedPreferencesProvider = sharedPreferencesProvider;
  }

  @Override
  public UploadUserPhotoUseCase get() {
    return newInstance(userRepositoryProvider.get(), sharedPreferencesProvider.get());
  }

  public static UploadUserPhotoUseCase_Factory create(
      Provider<UserRepository> userRepositoryProvider,
      Provider<SharedPreferences> sharedPreferencesProvider) {
    return new UploadUserPhotoUseCase_Factory(userRepositoryProvider, sharedPreferencesProvider);
  }

  public static UploadUserPhotoUseCase newInstance(UserRepository userRepository,
      SharedPreferences sharedPreferences) {
    return new UploadUserPhotoUseCase(userRepository, sharedPreferences);
  }
}
