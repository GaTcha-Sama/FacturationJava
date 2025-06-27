package com.facturation;

import com.facturation.model.Prestation;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestCalculs {
    
    @Test
    public void testCalculFormation() {
        Prestation prestation = new Prestation();
        prestation.setType("formation");
        prestation.setHeureDebut(LocalTime.of(9, 0));
        prestation.setHeureFin(LocalTime.of(17, 0));
        
        prestation.calculerMontant();
        
        // 8 heures * 70€ = 560€
        assertEquals(560.0, prestation.getMontantCalcule(), 0.01);
    }
    
    @Test
    public void testCalculConsultation() {
        Prestation prestation = new Prestation();
        prestation.setType("consultation");
        prestation.setTjm(350.0);
        
        prestation.calculerMontant();
        
        // TJM * 1 jour = 350€
        assertEquals(350.0, prestation.getMontantCalcule(), 0.01);
    }
    
    @Test
    public void testCalculFormationPartielle() {
        Prestation prestation = new Prestation();
        prestation.setType("formation");
        prestation.setHeureDebut(LocalTime.of(14, 0));
        prestation.setHeureFin(LocalTime.of(18, 0));
        
        prestation.calculerMontant();
        
        // 4 heures * 70€ = 280€
        assertEquals(280.0, prestation.getMontantCalcule(), 0.01);
    }
} 