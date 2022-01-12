// Generated by Dagger (https://dagger.dev).
package com.visitegypt.data.repository;

import com.visitegypt.data.source.remote.RetrofitService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import javax.inject.Provider;

@DaggerGenerated
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class ItemRepositoryImp_Factory implements Factory<ItemRepositoryImp> {
  private final Provider<RetrofitService> retrofitServiceProvider;

  public ItemRepositoryImp_Factory(Provider<RetrofitService> retrofitServiceProvider) {
    this.retrofitServiceProvider = retrofitServiceProvider;
  }

  @Override
  public ItemRepositoryImp get() {
    return newInstance(retrofitServiceProvider.get());
  }

  public static ItemRepositoryImp_Factory create(
      Provider<RetrofitService> retrofitServiceProvider) {
    return new ItemRepositoryImp_Factory(retrofitServiceProvider);
  }

  public static ItemRepositoryImp newInstance(RetrofitService retrofitService) {
    return new ItemRepositoryImp(retrofitService);
  }
}
