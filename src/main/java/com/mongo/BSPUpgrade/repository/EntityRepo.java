//package com.mongo.BSPUpgrade.repository;
//
//import java.util.List;
//
//import com.mongo.BSPUpgrade.entity.MongoServiceInf;
//import org.springframework.data.mongodb.repository.Query; // ✅ CORRECT
//import org.springframework.data.mongodb.repository.MongoRepository;
//
//public interface EntityRepo extends MongoRepository<MongoServiceInf,String> {
//
//    @Query(value = "{ 'status': ?0 }", fields = "{ 'serviceName' : 1, '_id' : 0 }")
//     List<MongoServiceInf> findServiceNamesByStatus(String status , String databaseName);
//}
