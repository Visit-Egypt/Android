// Generated by Dagger (https://dagger.dev).
package com.visitegypt.di;

import com.visitegypt.data.source.remote.RetrofitService;
import com.visitegypt.domain.repository.PostsRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

@DaggerGenerated
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class NetworkModule_ProvidePostRepositoryFactory implements Factory<PostsRepository> {
  private final NetworkModule module;

  private final Provider<RetrofitService> retrofitServiceProvider;

  public NetworkModule_ProvidePostRepositoryFactory(NetworkModule module,
      Provider<RetrofitService> retrofitServiceProvider) {
    this.module = module;
    this.retrofitServiceProvider = retrofitServiceProvider;
  }

  @Override
  public PostsRepository get() {
    return providePostRepository(module, retrofitServiceProvider.get());
  }

  public static NetworkModule_ProvidePostRepositoryFactory create(NetworkModule module,
      Provider<RetrofitService> retrofitServiceProvider) {
    return new NetworkModule_ProvidePostRepositoryFactory(module, retrofitServiceProvider);
  }

  public static PostsRepository providePostRepository(NetworkModule instance,
      RetrofitService retrofitService) {
    return Preconditions.checkNotNullFromProvides(instance.providePostRepository(retrofitService));
  }
}
