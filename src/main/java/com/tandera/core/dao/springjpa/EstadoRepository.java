package com.tandera.core.dao.springjpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.tandera.core.model.comercial.Estado;

public interface EstadoRepository extends JpaRepository<Estado , Integer> {
	
	public List<Estado> findByDescrContainingIgnoreCase(String descr);
	
	@Transactional
    @Modifying
    @Query("DELETE from Estado c where c.id = :id")
    public void deleteItemCategoria(@Param("id") Integer id); 
	

}
