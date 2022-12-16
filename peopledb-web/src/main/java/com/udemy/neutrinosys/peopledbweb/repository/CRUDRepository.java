package com.udemy.neutrinosys.peopledbweb.repository;

import com.udemy.neutrinosys.peopledbweb.bizmodel.Person;

import javax.persistence.Entity;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.udemy.neutrinosys.peopledbweb.repository.PeopleRepository.FIND_BY_ID_SQL;

abstract class CRUDRepository<T extends Entity> {
    protected Connection connection;
    //which parts are unique when creating a repository class,which parts are not unique(same)

    public CRUDRepository(Connection connection) {
        this.connection = connection;
    }

    public T save(T entity) {
        try {
//            String getSaveSql; is call to a method
            PreparedStatement ps = connection.prepareStatement(getSaveSql(), Statement.RETURN_GENERATED_KEYS);
            mapForSave(entity, ps);

            int RecordAffected = ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
                long id = rs.getLong(1);
//                entity.setId(id);
                System.out.println(entity);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entity;
    }

    public Optional<T> findById(Long id) throws SQLException {
        T entity = null;


        try {
            PreparedStatement ps = connection.prepareStatement(getFindByIdSql());
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                entity = extractEntityFromResultSet(rs);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(entity);

    }


    /**
     * @return Returns a string  that represents the SQL needed to retrieve one entity .
     * The SQL must contain one SQL parameter , i.e. "?" that will bind to the entity's ID
     */

    private String getFindByIdSql() {
        return FIND_BY_ID_SQL;
    }


    abstract void mapForSave(T entity, PreparedStatement ps) throws SQLException;

    abstract String getSaveSql();


    public List<T> findAll() {
        List<T> entities = new ArrayList<>();
      //rename people to Entities,collection plural form of our entities/model real world of OOP

        try {

            PreparedStatement ps = connection.prepareStatement(getFindAllSql());
//        we need to template the FIND_ALL_SQL
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
//                T entity = null;

                entities.add(extractEntityFromResultSet(rs));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entities;
    }
    public long count() {

        long count = 0;
        try {

            PreparedStatement ps =  connection.prepareStatement(getCountSql());
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                count = rs.getLong(1);

            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return count;
    }


    //delete method

    public  void delete(T entity){
        //...people equivalent of using variable argument to create an array Person[] people;
        try {
            PreparedStatement ps = connection.prepareStatement(getDeleteSql());
            ps.setLong(1,entity.getId());
            ResultSet affectedRecordCount = ps.executeQuery();

            System.out.println(affectedRecordCount);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getDeleteSql() {
        return getDeleteSql();
    }


    protected abstract String getFindAllSql();
    protected abstract String getCountSql();
    protected String  getDeleteSql;
    abstract T extractEntityFromResultSet(ResultSet rs) throws SQLException;

}
