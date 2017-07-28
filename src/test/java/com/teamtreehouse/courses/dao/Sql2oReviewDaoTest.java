package com.teamtreehouse.courses.dao;

import static org.junit.Assert.*;

import com.teamtreehouse.courses.exc.DaoException;
import com.teamtreehouse.courses.model.Course;
import com.teamtreehouse.courses.model.Review;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;

public class Sql2oReviewDaoTest {
  private Sql2oCourseDao courseDao;
  private Sql2oReviewDao reviewDao;
  private Connection conn;
  private Course course;
  @Before
  public void setUp() throws Exception {
    String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/init.sql'";
    Sql2o sql2o = new Sql2o(connectionString, "", "");
    reviewDao = new Sql2oReviewDao(sql2o);
    courseDao = new Sql2oCourseDao(sql2o);
    // Keep connection open through entire test so that isn't wiped out.
    conn = sql2o.open();
    course = new Course("Test", "http://test.com");
    courseDao.add(course);
  }

  @After
  public void tearDown() throws Exception {
    conn.close();
  }


  @Test
  public void addingReviewSetsNewId() throws Exception {
    Review review = new Review(course.getId(), 5, "test comment");
    int originalId = review.getId();
    reviewDao.add(review);
    assertNotEquals(originalId, review.getId());
  }

  @Test
  public void multipleReviewsAreFoundWhenTheyExistForACourse() throws Exception {
    reviewDao.add(new Review(course.getId(), 5, "test comment 1"));
    reviewDao.add(new Review(course.getId(), 1, "test comment 2"));

    List<Review> reviews = reviewDao.findByCourseId(course.getId());
    assertEquals(2, reviews.size());
  }

  @Test(expected = DaoException.class)
  public void addingAReviewToANonExistingCourseFails() throws Exception {
    Review review = new Review(42, 5, "test comment");
    reviewDao.add(review);

  }

}