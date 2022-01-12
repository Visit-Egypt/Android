// Generated by Dagger (https://dagger.dev).
package com.visitegypt.domain.usecase;

import com.visitegypt.domain.repository.PlaceRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import javax.inject.Provider;

@DaggerGenerated
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class GetPlacesUseCase_Factory implements Factory<GetPlacesUseCase> {
  private final Provider<PlaceRepository> placeRepositoryProvider;

  public GetPlacesUseCase_Factory(Provider<PlaceRepository> placeRepositoryProvider) {
    this.placeRepositoryProvider = placeRepositoryProvider;
  }

  @Override
  public GetPlacesUseCase get() {
    return newInstance(placeRepositoryProvider.get());
  }

  public static GetPlacesUseCase_Factory create(Provider<PlaceRepository> placeRepositoryProvider) {
    return new GetPlacesUseCase_Factory(placeRepositoryProvider);
  }

  public static GetPlacesUseCase newInstance(PlaceRepository placeRepository) {
    return new GetPlacesUseCase(placeRepository);
  }
}
