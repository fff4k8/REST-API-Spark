package com.teamtreehouse.courses.dao;

import com.teamtreehouse.courses.exc.DaoException;
import com.teamtreehouse.courses.model.Course;

import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.Arrays;
import java.util.List;

public class Sql2oCourseDao implements CourseDao {

  private final Sql2o sql2o;

  public Sql2oCourseDao(Sql2o sql2o) {
    this.sql2o = sql2o;

  }

  public void add(Course course) throws DaoException {
    String sql = "INSERT INTO courses(name, url) VALUES (:name, :url)";
    try (Connection con = sql2o.open()) {
      int id = (int) con.createQuery(sql)
          .bind(course)
          .executeUpdate()
          .getKey();
      course.setId(id);
    } catch (Sql2oException ex) {
      throw new DaoException(ex, "Adding course problem");
    }
  }

  public List<Course> findAll() {
    try (Connection con = sql2o.open()){
      return con.createQuery("SELECT * FROM courses")
          .executeAndFetch(Course.class);
    }


  }

  @Override
  public Course findById(int id) {
   try(Connection con = sql2o.open()){
     return con.createQuery("SELECT * FROM courses WHERE id = :id")
         .addParameter("id",id)
         .executeAndFetchFirst(Course.class);
   }
  }
}
