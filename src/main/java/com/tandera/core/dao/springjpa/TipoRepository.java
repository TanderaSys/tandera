package com.tandera.core.dao.springjpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.tandera.core.model.comercial.Tipo;

public interface TipoRepository extends JpaRepository<Tipo, Integer> {
	
	public List<Tipo> findByDescrContainingIgnoreCase(String descr);
	
	public Tipo findById(Integer id);
	
	@Transactional
    @Modifying
    @Query("DELETE from Tipo c where c.id = :id")
    public void deleteItemCategoria(@Param("id") Integer id); 

}
