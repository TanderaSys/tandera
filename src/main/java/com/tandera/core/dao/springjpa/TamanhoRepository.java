package com.tandera.core.dao.springjpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.tandera.core.model.comercial.Tamanho;


public interface TamanhoRepository extends JpaRepository <Tamanho, Integer> {

	public List <Tamanho> findByDescrContainingIgnoreCase(String descr);
	
	public Tamanho findById(Integer id);
	
	@Query(nativeQuery=false, value="SELECT t from Tamanho t WHERE t.descr like :descr")
	public List<Tamanho> listarPorDescricao(@Param("descr") String descr);
	
	@Transactional
    @Modifying
    @Query("DELETE from Tamanho c where c.id = :id")
    public void deleteItemCategoria(@Param("id") Integer id); 

}
