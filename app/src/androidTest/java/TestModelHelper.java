import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.chk.newsapp.data.datamodel.SourceEntity;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by chira on 22-08-2017.
 */

public class TestModelHelper {

    public static SourceEntity getSourceEntity(String id, String name,String category) {
        SourceEntity sourceEntity = new SourceEntity();
        sourceEntity.setId(id);
        sourceEntity.setName(name);
        sourceEntity.setCategory(category);
        sourceEntity.setCountry("de");
        sourceEntity.setLanguage("en");
        sourceEntity.setUrl("http://test.com");
        return sourceEntity;
    }

    public static <T> T getValue(final LiveData<T> liveData) throws InterruptedException {
        final Object[] data = new Object[1];
        final CountDownLatch latch = new CountDownLatch(1);
        Observer<T> observer = new Observer<T>() {
            @Override
            public void onChanged(@Nullable T o) {
                data[0] = o;
                latch.countDown();
                liveData.removeObserver(this);
            }
        };
        liveData.observeForever(observer);
        latch.await(2, TimeUnit.SECONDS);
        //noinspection unchecked
        return (T) data[0];
    }
}
