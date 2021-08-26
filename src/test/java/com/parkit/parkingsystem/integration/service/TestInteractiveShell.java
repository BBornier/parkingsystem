package com.parkit.parkingsystem.integration.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.service.InteractiveShell;
import com.parkit.parkingsystem.util.InputReaderUtil;

class TestInteractiveShell {	
	
	
	@Test
	void testAppIsWellInitialized() {
		// ARRANGE
		InteractiveShell interactiveShell = new InteractiveShell();
		InteractiveShell.loadInterface();
		
	}
	
	@Test
	void testMenuIsLoadingWell() {
		InteractiveShell interactiveShell = new InteractiveShell();
		InteractiveShell.loadMenu();
	}

}
