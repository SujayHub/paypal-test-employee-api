package com.paypal.bfs.test.employeeserv.mapper;

public interface EntityBoMapper <K, T>{

  K toEntity(T t);
  T toBo(K k);

}
