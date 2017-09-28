import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.chk.newsapp.data.datamodel.HeadlinesEntity;
import com.chk.newsapp.data.datamodel.SourceEntity;
import com.chk.newsapp.data.db.dao.HeadlinesDao;
import com.chk.newsapp.data.db.dao.SourceDao;

/**
 * Created by chira on 15-08-2017.
 */
@Database(entities = {HeadlinesEntity.class, SourceEntity.class}, version = 1)
public abstract class TestDatabase extends RoomDatabase {

    public abstract SourceDao sourceDao();

    public abstract HeadlinesDao headlinesDao();
}
