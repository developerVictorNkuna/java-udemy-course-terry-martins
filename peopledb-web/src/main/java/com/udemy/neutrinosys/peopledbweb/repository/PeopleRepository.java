package com.udemy.neutrinosys.peopledbweb.repository;

import com.udemy.neutrinosys.peopledbweb.annotation.SQL;
import com.udemy.neutrinosys.peopledbweb.bizmodel.Person;

import java.math.BigDecimal;
import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.joining;

public class PeopleRepository  extends CRUDRepository<Person>{

    public static final String SAVE_PERSON_SQL="INSERT INTO PEOPLE (FIRST_NAME,LAST_NAME,DOB) VALUES(?,?,?)";
    public static final String FIND_BY_ID_SQL="SELECT ID, FIRST_NAME,LAST_NAME,DOB,SALARY FROM PEOPLE WHERE ID=?";
    public static final String FIND_ALL_SQL="SELECT ID,FIRST_NAME,LAST_NAME,DOB,SALARY FROM PEOPLE";
    public static final String SELECT_COUNT_FROM_PEOPLE="SELECT COUNT(*) FROM PEOPLE";
    public static final String DELETE_SQL="DELETE FROM PEOPLE WHERE ID=?";
    public static final String DELETE_IN_SQL="DELETE FROM PEOPLE WHERE ID IN (:ids)";
    public static final String UPDATE_SQL="UPDATE PEOPLE SET FIRST_NAME=?,LAST_NAME=?,DOB=?,SALARY=? WHERE ID=?";



    //    private Connection connection;

    public PeopleRepository(Connection connection) {
        super(connection);

    }

    public Person save(Person entity) {
        String sql = """
            INSERT INTO PEOPLE (FIRST_NAME, LAST_NAME,DOB,SALARY) VALUES(?,?,?,?)
""";
                //String.format(INSERT INTO PEOPLE (FIRST_NAME, LAST_NAME,DOB) VALUES(%s,%s,%s))

        try {
            //this code is for making MySQL connection to the db using jdbc
            String dbURL = "jdbc:mysql://localhost:3306/people?user=root&password=VINK4013@a";

            Connection connection = DriverManager.getConnection(dbURL);

            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setTimestamp(3,Timestamp.valueOf(entity.getDob().withZoneSameInstant(ZoneId.of("+0")).toLocalDateTime()));
            ps.setInt(4,0);

            int recordsAffected  = ps.executeUpdate();
            ResultSet rs =  ps.getGeneratedKeys();
            //get rows from db

            while(rs.next()) {
                rs.getLong(1);
                entity.setId(2l);
            }
            System.out.printf("Record affected: %d%n",recordsAffected);
            ps.setTimestamp(3, Timestamp.valueOf(
                    entity.getDob().withZoneSameInstant(ZoneId.of("+0")).toLocalDateTime()));
        } catch (SQLException sqlException){
            sqlException.printStackTrace();

        }
        return entity;
    }



    public List<Person> findAll() {
        String sql ="SELECT * FROM PEOPLE";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);

            ResultSet resultSet = ps.executeQuery();
            for(int i =0;resultSet.next();i++){
                System.out.println(resultSet.getObject(i));
            }

            } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    public List<Person> FindAll(){
        List<Person> people  = new ArrayList<>();
        try {
            PreparedStatement ps =  connection.prepareStatement(FIND_ALL_SQL);
            ResultSet rs =ps.executeQuery();
            while(rs.next()) {
                people.add(extractEntityFromResultSet(rs));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return people;
    }
    @Override
    protected String getFindAllSql() {
        return FIND_ALL_SQL;
    }

    @Override
    protected String getCountSql() {
        return SELECT_COUNT_FROM_PEOPLE;
    }


    public void deleteAllById(Iterable<Long> ids){
//        Iterable<Person> peopleToDelete = personRepository.findAll();
//        //cannot call indirectly stream iterable
//        Stream<Person> =  StreamSupport.stream(peopleToDelete.spliterator());

    }

    //delete method

    public  void delete(Person person){
        //...people equivalent of using variable argument to create an array Person[] people;
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM PEOPLE WHERE ID=?");
            ps.setLong(1,person.getId());
            ResultSet affectedRecordCount = ps.executeQuery();

            System.out.println(affectedRecordCount);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //delete array enhanced four loop

    public void deleteMethodOne(Person ...people){
    for(Person person  : people){

            deleteMethodOne(people);
        }
    }

    public void deleteMethodTwo(Person...people){
        try {
            Statement stmt = connection.createStatement();
            String ids = Arrays.stream(people).map(Person::getId).map(String::valueOf).collect(joining(","));
            stmt.executeUpdate("DELETE FROM PEOPLE WHERE ID IN (:ids)".replace("ids",ids));
            //do need to know how many people have ids with this method
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //delete using  in clause

    public  void delete (Person ...people){
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("DELETE FROM PEOPLE WHERE ID IN (?,?,?,?)");

        }catch(SQLException e){
        e.printStackTrace();
        }
    }
    

    public Optional<Person> findById(Long id) throws SQLException {
        Person person = null;


        try{
            PreparedStatement ps = connection.prepareStatement(FIND_BY_ID_SQL);
            ps.setLong(1,id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                person =  extractEntityFromResultSet(rs);
            }


        }catch(SQLException e){
            e.printStackTrace();
        }

        return Optional.ofNullable(person);

    }

//    private Person extractEntityFromResultSet(ResultSet rs) {
//    }

    @Override
    Person extractEntityFromResultSet(ResultSet rs) throws  SQLException {
        long personId =  rs.getLong("ID");
        String firstName= rs.getString("FIRST_NAME");
        String lastName= rs.getString("LAST_NAME");
        ZonedDateTime dob =  ZonedDateTime.of(rs.getTimestamp("DOB").toLocalDateTime(),ZoneId.of("+0"));
        int Salary = rs.getInt("SALARY");
        return new Person(personId,firstName,lastName,dob, BigDecimal.valueOf(Salary));

    }
    //211: Creating custom annotation  of SQL so that our program will know about it at runtime
    @Override
    @SQL("INSERT INTO PEOPLE (FIRST_NAME,LAST_NAME,DOB) VALUES(?,?,?)")
    void mapForSave(Person entity, PreparedStatement ps) throws SQLException {
        ps.setString(1,entity.getFirstName());
        ps.setString(1,entity.getLastName());
        ps.setTimestamp(3,convertDobToTimeStamp(entity.getDob()));
        

    }

    private Timestamp convertDobToTimeStamp(ZonedDateTime dob) {
    }

    @Override
    String getSaveSql() {
        return SAVE_PERSON_SQL;
    }

//    public long count() {
//
//        long count = 0;
//        try {
//
//            PreparedStatement ps =  connection.prepareStatement("SELECT COUNT(*) FROM PEOPLE");
//            ResultSet rs = ps.executeQuery();
//            if(rs.next()) {
//                count = rs.getLong(1);
//
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//
//        }
//        return count;
//    }

    public void update(Person person) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE PEOPLE SET FIRST_NAME=?,LAST_NAME=?,DOB=?,SALARY=?");
            ps.setString(1,person.getFirstName());
            ps.setString(2,person.convertDobToTimeStamp(person.getDob()));
//            ps.setTimestamp(3,person.());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

