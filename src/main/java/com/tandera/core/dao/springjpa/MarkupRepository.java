package com.tandera.core.dao.springjpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.tandera.core.model.comercial.Markup;

public interface MarkupRepository extends JpaRepository<Markup, Integer> {
	
	public List<Markup> findBySiglaContainingIgnoreCase(String sigla);
	
	public Markup findById(Integer id);
	
	@Transactional
    @Modifying
    @Query("DELETE from Markup c where c.id = :id")
    public void deleteItemCategoria(@Param("id") Integer id); 

}
