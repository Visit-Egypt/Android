package com.visitegypt.data.source.local.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.visitegypt.domain.model.Item;
import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.Review;
import com.visitegypt.domain.model.converters.CategoriesConverter;
import com.visitegypt.domain.model.converters.ImageUrlsConverter;
import com.visitegypt.domain.model.converters.ItemsTypeConverter;
import com.visitegypt.domain.model.converters.OpeningHoursConverter;
import com.visitegypt.domain.model.converters.ReviewsConverter;
import com.visitegypt.domain.model.converters.TicketPricesConverter;
import java.lang.Class;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unchecked", "deprecation"})
public final class PlaceDao_Impl implements PlaceDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Place> __insertionAdapterOfPlace;

  public PlaceDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPlace = new EntityInsertionAdapter<Place>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `Place` (`id`,`title`,`longDescription`,`shortDescription`,`locationDescription`,`longitude`,`altitude`,`city`,`defaultImage`,`categories`,`items`,`imageUrls`,`ticketPrices`,`openingHours`,`reviews`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Place value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getId());
        }
        if (value.getTitle() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getTitle());
        }
        if (value.getLongDescription() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getLongDescription());
        }
        if (value.getShortDescription() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getShortDescription());
        }
        if (value.getLocationDescription() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getLocationDescription());
        }
        stmt.bindDouble(6, value.getLongitude());
        stmt.bindDouble(7, value.getAltitude());
        if (value.getCity() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getCity());
        }
        if (value.getDefaultImage() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getDefaultImage());
        }
        final String _tmp;
        _tmp = CategoriesConverter.categoryToString(value.getCategories());
        if (_tmp == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, _tmp);
        }
        final String _tmp_1;
        _tmp_1 = ItemsTypeConverter.itemsToString(value.getItems());
        if (_tmp_1 == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, _tmp_1);
        }
        final String _tmp_2;
        _tmp_2 = ImageUrlsConverter.imageUrlsToString(value.getImageUrls());
        if (_tmp_2 == null) {
          stmt.bindNull(12);
        } else {
          stmt.bindString(12, _tmp_2);
        }
        final String _tmp_3;
        _tmp_3 = TicketPricesConverter.fromStringMap(value.getTicketPrices());
        if (_tmp_3 == null) {
          stmt.bindNull(13);
        } else {
          stmt.bindString(13, _tmp_3);
        }
        final String _tmp_4;
        _tmp_4 = OpeningHoursConverter.fromStringMap(value.getOpeningHours());
        if (_tmp_4 == null) {
          stmt.bindNull(14);
        } else {
          stmt.bindString(14, _tmp_4);
        }
        final String _tmp_5;
        _tmp_5 = ReviewsConverter.reviewsToString(value.getReviews());
        if (_tmp_5 == null) {
          stmt.bindNull(15);
        } else {
          stmt.bindString(15, _tmp_5);
        }
      }
    };
  }

  @Override
  public void insert(final Place place) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfPlace.insert(place);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Place> getALLPlaces() {
    final String _sql = "SELECT * FROM Place";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfLongDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "longDescription");
      final int _cursorIndexOfShortDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "shortDescription");
      final int _cursorIndexOfLocationDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "locationDescription");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfAltitude = CursorUtil.getColumnIndexOrThrow(_cursor, "altitude");
      final int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
      final int _cursorIndexOfDefaultImage = CursorUtil.getColumnIndexOrThrow(_cursor, "defaultImage");
      final int _cursorIndexOfCategories = CursorUtil.getColumnIndexOrThrow(_cursor, "categories");
      final int _cursorIndexOfItems = CursorUtil.getColumnIndexOrThrow(_cursor, "items");
      final int _cursorIndexOfImageUrls = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrls");
      final int _cursorIndexOfTicketPrices = CursorUtil.getColumnIndexOrThrow(_cursor, "ticketPrices");
      final int _cursorIndexOfOpeningHours = CursorUtil.getColumnIndexOrThrow(_cursor, "openingHours");
      final int _cursorIndexOfReviews = CursorUtil.getColumnIndexOrThrow(_cursor, "reviews");
      final List<Place> _result = new ArrayList<Place>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Place _item;
        _item = new Place();
        final String _tmpId;
        if (_cursor.isNull(_cursorIndexOfId)) {
          _tmpId = null;
        } else {
          _tmpId = _cursor.getString(_cursorIndexOfId);
        }
        _item.setId(_tmpId);
        final String _tmpTitle;
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _tmpTitle = null;
        } else {
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        }
        _item.setTitle(_tmpTitle);
        final String _tmpLongDescription;
        if (_cursor.isNull(_cursorIndexOfLongDescription)) {
          _tmpLongDescription = null;
        } else {
          _tmpLongDescription = _cursor.getString(_cursorIndexOfLongDescription);
        }
        _item.setLongDescription(_tmpLongDescription);
        final String _tmpShortDescription;
        if (_cursor.isNull(_cursorIndexOfShortDescription)) {
          _tmpShortDescription = null;
        } else {
          _tmpShortDescription = _cursor.getString(_cursorIndexOfShortDescription);
        }
        _item.setShortDescription(_tmpShortDescription);
        final String _tmpLocationDescription;
        if (_cursor.isNull(_cursorIndexOfLocationDescription)) {
          _tmpLocationDescription = null;
        } else {
          _tmpLocationDescription = _cursor.getString(_cursorIndexOfLocationDescription);
        }
        _item.setLocationDescription(_tmpLocationDescription);
        final double _tmpLongitude;
        _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
        _item.setLongitude(_tmpLongitude);
        final double _tmpAltitude;
        _tmpAltitude = _cursor.getDouble(_cursorIndexOfAltitude);
        _item.setAltitude(_tmpAltitude);
        final String _tmpCity;
        if (_cursor.isNull(_cursorIndexOfCity)) {
          _tmpCity = null;
        } else {
          _tmpCity = _cursor.getString(_cursorIndexOfCity);
        }
        _item.setCity(_tmpCity);
        final String _tmpDefaultImage;
        if (_cursor.isNull(_cursorIndexOfDefaultImage)) {
          _tmpDefaultImage = null;
        } else {
          _tmpDefaultImage = _cursor.getString(_cursorIndexOfDefaultImage);
        }
        _item.setDefaultImage(_tmpDefaultImage);
        final List<String> _tmpCategories;
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfCategories)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfCategories);
        }
        _tmpCategories = CategoriesConverter.stringToCategory(_tmp);
        _item.setCategories(_tmpCategories);
        final List<Item> _tmpItems;
        final String _tmp_1;
        if (_cursor.isNull(_cursorIndexOfItems)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getString(_cursorIndexOfItems);
        }
        _tmpItems = ItemsTypeConverter.stringToItems(_tmp_1);
        _item.setItems(_tmpItems);
        final List<String> _tmpImageUrls;
        final String _tmp_2;
        if (_cursor.isNull(_cursorIndexOfImageUrls)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getString(_cursorIndexOfImageUrls);
        }
        _tmpImageUrls = ImageUrlsConverter.StringToImageUrls(_tmp_2);
        _item.setImageUrls(_tmpImageUrls);
        final Map<String, Integer> _tmpTicketPrices;
        final String _tmp_3;
        if (_cursor.isNull(_cursorIndexOfTicketPrices)) {
          _tmp_3 = null;
        } else {
          _tmp_3 = _cursor.getString(_cursorIndexOfTicketPrices);
        }
        _tmpTicketPrices = TicketPricesConverter.fromString(_tmp_3);
        _item.setTicketPrices(_tmpTicketPrices);
        final Map<String, String> _tmpOpeningHours;
        final String _tmp_4;
        if (_cursor.isNull(_cursorIndexOfOpeningHours)) {
          _tmp_4 = null;
        } else {
          _tmp_4 = _cursor.getString(_cursorIndexOfOpeningHours);
        }
        _tmpOpeningHours = OpeningHoursConverter.fromString(_tmp_4);
        _item.setOpeningHours(_tmpOpeningHours);
        final List<Review> _tmpReviews;
        final String _tmp_5;
        if (_cursor.isNull(_cursorIndexOfReviews)) {
          _tmp_5 = null;
        } else {
          _tmp_5 = _cursor.getString(_cursorIndexOfReviews);
        }
        _tmpReviews = ReviewsConverter.stringToReviews(_tmp_5);
        _item.setReviews(_tmpReviews);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Place getPlaceByTitle(final String title) {
    final String _sql = "SELECT * FROM Place WHERE title=(?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (title == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, title);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfLongDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "longDescription");
      final int _cursorIndexOfShortDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "shortDescription");
      final int _cursorIndexOfLocationDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "locationDescription");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfAltitude = CursorUtil.getColumnIndexOrThrow(_cursor, "altitude");
      final int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
      final int _cursorIndexOfDefaultImage = CursorUtil.getColumnIndexOrThrow(_cursor, "defaultImage");
      final int _cursorIndexOfCategories = CursorUtil.getColumnIndexOrThrow(_cursor, "categories");
      final int _cursorIndexOfItems = CursorUtil.getColumnIndexOrThrow(_cursor, "items");
      final int _cursorIndexOfImageUrls = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrls");
      final int _cursorIndexOfTicketPrices = CursorUtil.getColumnIndexOrThrow(_cursor, "ticketPrices");
      final int _cursorIndexOfOpeningHours = CursorUtil.getColumnIndexOrThrow(_cursor, "openingHours");
      final int _cursorIndexOfReviews = CursorUtil.getColumnIndexOrThrow(_cursor, "reviews");
      final Place _result;
      if(_cursor.moveToFirst()) {
        _result = new Place();
        final String _tmpId;
        if (_cursor.isNull(_cursorIndexOfId)) {
          _tmpId = null;
        } else {
          _tmpId = _cursor.getString(_cursorIndexOfId);
        }
        _result.setId(_tmpId);
        final String _tmpTitle;
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _tmpTitle = null;
        } else {
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        }
        _result.setTitle(_tmpTitle);
        final String _tmpLongDescription;
        if (_cursor.isNull(_cursorIndexOfLongDescription)) {
          _tmpLongDescription = null;
        } else {
          _tmpLongDescription = _cursor.getString(_cursorIndexOfLongDescription);
        }
        _result.setLongDescription(_tmpLongDescription);
        final String _tmpShortDescription;
        if (_cursor.isNull(_cursorIndexOfShortDescription)) {
          _tmpShortDescription = null;
        } else {
          _tmpShortDescription = _cursor.getString(_cursorIndexOfShortDescription);
        }
        _result.setShortDescription(_tmpShortDescription);
        final String _tmpLocationDescription;
        if (_cursor.isNull(_cursorIndexOfLocationDescription)) {
          _tmpLocationDescription = null;
        } else {
          _tmpLocationDescription = _cursor.getString(_cursorIndexOfLocationDescription);
        }
        _result.setLocationDescription(_tmpLocationDescription);
        final double _tmpLongitude;
        _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
        _result.setLongitude(_tmpLongitude);
        final double _tmpAltitude;
        _tmpAltitude = _cursor.getDouble(_cursorIndexOfAltitude);
        _result.setAltitude(_tmpAltitude);
        final String _tmpCity;
        if (_cursor.isNull(_cursorIndexOfCity)) {
          _tmpCity = null;
        } else {
          _tmpCity = _cursor.getString(_cursorIndexOfCity);
        }
        _result.setCity(_tmpCity);
        final String _tmpDefaultImage;
        if (_cursor.isNull(_cursorIndexOfDefaultImage)) {
          _tmpDefaultImage = null;
        } else {
          _tmpDefaultImage = _cursor.getString(_cursorIndexOfDefaultImage);
        }
        _result.setDefaultImage(_tmpDefaultImage);
        final List<String> _tmpCategories;
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfCategories)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfCategories);
        }
        _tmpCategories = CategoriesConverter.stringToCategory(_tmp);
        _result.setCategories(_tmpCategories);
        final List<Item> _tmpItems;
        final String _tmp_1;
        if (_cursor.isNull(_cursorIndexOfItems)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getString(_cursorIndexOfItems);
        }
        _tmpItems = ItemsTypeConverter.stringToItems(_tmp_1);
        _result.setItems(_tmpItems);
        final List<String> _tmpImageUrls;
        final String _tmp_2;
        if (_cursor.isNull(_cursorIndexOfImageUrls)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getString(_cursorIndexOfImageUrls);
        }
        _tmpImageUrls = ImageUrlsConverter.StringToImageUrls(_tmp_2);
        _result.setImageUrls(_tmpImageUrls);
        final Map<String, Integer> _tmpTicketPrices;
        final String _tmp_3;
        if (_cursor.isNull(_cursorIndexOfTicketPrices)) {
          _tmp_3 = null;
        } else {
          _tmp_3 = _cursor.getString(_cursorIndexOfTicketPrices);
        }
        _tmpTicketPrices = TicketPricesConverter.fromString(_tmp_3);
        _result.setTicketPrices(_tmpTicketPrices);
        final Map<String, String> _tmpOpeningHours;
        final String _tmp_4;
        if (_cursor.isNull(_cursorIndexOfOpeningHours)) {
          _tmp_4 = null;
        } else {
          _tmp_4 = _cursor.getString(_cursorIndexOfOpeningHours);
        }
        _tmpOpeningHours = OpeningHoursConverter.fromString(_tmp_4);
        _result.setOpeningHours(_tmpOpeningHours);
        final List<Review> _tmpReviews;
        final String _tmp_5;
        if (_cursor.isNull(_cursorIndexOfReviews)) {
          _tmp_5 = null;
        } else {
          _tmp_5 = _cursor.getString(_cursorIndexOfReviews);
        }
        _tmpReviews = ReviewsConverter.stringToReviews(_tmp_5);
        _result.setReviews(_tmpReviews);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
