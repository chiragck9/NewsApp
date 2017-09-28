import android.support.test.runner.AndroidJUnit4;

import com.chk.newsapp.data.datamodel.SourceEntity;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created by chira on 22-08-2017.
 */

@RunWith(AndroidJUnit4.class)
public class SourcesDaoTest extends BaseDBTest {


    @Test
    public void insert() throws InterruptedException {

        List<SourceEntity> sourceEntities = new ArrayList<>();
        sourceEntities.add(TestModelHelper.getSourceEntity("id1", "abc1", "general"));
        sourceEntities.add(TestModelHelper.getSourceEntity("id2", "abc2", "sport"));
        sourceEntities.add(TestModelHelper.getSourceEntity("id3", "abc3", "politics"));
        sourceEntities.add(TestModelHelper.getSourceEntity("id4", "abc4", "general"));
        db.sourceDao().insert(sourceEntities);

        List<SourceEntity> sourceEntitiesResult = TestModelHelper.getValue(db.sourceDao().getSources());

        assertEquals(sourceEntities.size(), sourceEntitiesResult.size());
        assertEquals(sourceEntities.get(0).getId(), sourceEntitiesResult.get(0).getId());
        assertEquals(sourceEntities.get(1).getId(), sourceEntitiesResult.get(1).getId());
        assertEquals(sourceEntities.get(2).getId(), sourceEntitiesResult.get(2).getId());
        assertEquals(sourceEntities.get(3).getId(), sourceEntitiesResult.get(3).getId());
        assertEquals(sourceEntities.get(0).getName(), sourceEntitiesResult.get(0).getName());
        assertEquals(sourceEntities.get(1).getName(), sourceEntitiesResult.get(1).getName());
        assertEquals(sourceEntities.get(2).getName(), sourceEntitiesResult.get(2).getName());
        assertEquals(sourceEntities.get(3).getName(), sourceEntitiesResult.get(3).getName());
        assertEquals(sourceEntities.get(0).getCategory(), sourceEntitiesResult.get(0).getCategory());
        assertEquals(sourceEntities.get(1).getCategory(), sourceEntitiesResult.get(1).getCategory());
        assertEquals(sourceEntities.get(2).getCategory(), sourceEntitiesResult.get(2).getCategory());
        assertEquals(sourceEntities.get(3).getCategory(), sourceEntitiesResult.get(3).getCategory());
    }

    @Test
    public void setSubscribed() throws InterruptedException {
        List<SourceEntity> sourceEntities = new ArrayList<>();
        sourceEntities.add(TestModelHelper.getSourceEntity("id1", "abc1", "general"));
        sourceEntities.add(TestModelHelper.getSourceEntity("id2", "abc2", "politics"));
        sourceEntities.add(TestModelHelper.getSourceEntity("id3", "abc3", "general"));
        sourceEntities.add(TestModelHelper.getSourceEntity("id4", "abc4", "sport"));
        db.sourceDao().insert(sourceEntities);

        assertEquals(false, sourceEntities.get(0).isSubscribed());

        db.sourceDao().setSubscribed("id1", true);

        List<SourceEntity> sourceEntitiesResult = TestModelHelper.getValue(db.sourceDao().getSources());

        assertEquals(sourceEntities.size(), sourceEntitiesResult.size());
        assertEquals(sourceEntities.get(0).getId(), sourceEntitiesResult.get(0).getId());
        assertEquals(true, sourceEntitiesResult.get(0).isSubscribed());
    }

    @Test
    public void getSourcesByCategory() throws InterruptedException {
        List<SourceEntity> sourceEntities = new ArrayList<>();
        sourceEntities.add(TestModelHelper.getSourceEntity("id1", "abc1", "general"));
        sourceEntities.add(TestModelHelper.getSourceEntity("id2", "abc2", "politics"));
        sourceEntities.add(TestModelHelper.getSourceEntity("id3", "abc3", "general"));
        sourceEntities.add(TestModelHelper.getSourceEntity("id4", "abc4", "sport"));
        db.sourceDao().insert(sourceEntities);

        assertEquals(false, sourceEntities.get(0).isSubscribed());

        db.sourceDao().setSubscribed("id3", true);

        List<SourceEntity> sourceEntitiesResult = TestModelHelper.getValue(db.sourceDao().getSourcesByCategory("general"));

        assertEquals(2, sourceEntitiesResult.size());
        assertEquals(sourceEntities.get(0).getId(), sourceEntitiesResult.get(0).getId());
        assertEquals(sourceEntities.get(2).getId(), sourceEntitiesResult.get(1).getId());
        assertEquals(false, sourceEntitiesResult.get(0).isSubscribed());
        assertEquals(true, sourceEntitiesResult.get(1).isSubscribed());
    }
}
