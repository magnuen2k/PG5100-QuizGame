package org.quizgame.ejb;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quizgame.entity.Category;
import org.quizgame.entity.SubCategory;

import javax.ejb.EJB;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class CategoryEjbTest {

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, "org.quizgame")
                .addAsResource("META-INF/persistence.xml");
    }

    @EJB
    private CategoryEjb categoryEjb;

    @EJB
    private ResetEjb resetEjb;

    @Before
    public void init(){
        resetEjb.resetDatabase();
    }

    @Test
    public void testNoCategory() {
        List<Category> categories = categoryEjb.getAllCategories(false);
        assertEquals(0, categories.size());
    }

    @Test
    public void testCreateCategory() {
        Long id = categoryEjb.createCategory("test");
        assertNotNull(id);
    }

    @Test
    public void testGetCategory() {
        Long id = categoryEjb.createCategory("test");
        Category test = categoryEjb.getCategory(id, false);
        assertEquals("test", test.getName());
    }

    @Test
    public void testCreateSubCategory() {
        Long parentId = categoryEjb.createCategory("test");
        Long id = categoryEjb.createSubCategory(parentId, "test sub");
        SubCategory sc = categoryEjb.getSubCategory(id);
        assertEquals("test sub", sc.getName());
        assertEquals(parentId, sc.getCategory().getId());
        assertNotNull(id);
    }

    @Test
    public void testGetAllCategories() {
        Long c1 = categoryEjb.createCategory("c1");
        Long c2 = categoryEjb.createCategory("c2");
        Long c3 = categoryEjb.createCategory("c3");

        categoryEjb.createSubCategory(c1, "sc1");
        categoryEjb.createSubCategory(c2, "sc2");
        categoryEjb.createSubCategory(c3, "sc3");

        List<Category> categories = categoryEjb.getAllCategories(false);
        assertEquals(3, categories.size());

        Category c = categories.get(0);

        try {
            c.getSubCategories().size();
            fail();
        } catch (Exception e) {

        }

        categories = categoryEjb.getAllCategories(true);
        c = categories.get(0);
        assertEquals(1, c.getSubCategories().size());
    }

    @Test
    public void testCreateTwice() {
        categoryEjb.createCategory("1");

        try {
            categoryEjb.createCategory("1");
            fail();
        } catch (Exception e) {

        }
    }
}
