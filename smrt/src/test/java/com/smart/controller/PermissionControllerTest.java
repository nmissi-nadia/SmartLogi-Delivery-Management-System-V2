package com.smart.controller;

import com.smart.dto.PermissionDTO;
import com.smart.service.PermissionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PermissionControllerTest {

    @Mock
    private PermissionService permissionService;

    @InjectMocks
    private PermissionController permissionController;

    private PermissionDTO permissionDTO;

    @BeforeEach
    void setUp() {
        permissionDTO = new PermissionDTO();
        permissionDTO.setId("1");
        permissionDTO.setName("TEST_PERMISSION");
    }

    @Test
    void testGetAllPermissions() {
        when(permissionService.getAllPermissions()).thenReturn(Collections.singletonList(permissionDTO));

        ResponseEntity<List<PermissionDTO>> response = permissionController.getAllPermissions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("TEST_PERMISSION", response.getBody().get(0).getName());
        verify(permissionService, times(1)).getAllPermissions();
    }

    @Test
    void testCreatePermission() {
        when(permissionService.createPermission(any(PermissionDTO.class))).thenReturn(permissionDTO);

        ResponseEntity<PermissionDTO> response = permissionController.createPermission(new PermissionDTO());

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("TEST_PERMISSION", response.getBody().getName());
        verify(permissionService, times(1)).createPermission(any(PermissionDTO.class));
    }

    @Test
    void testDeletePermission() {
        doNothing().when(permissionService).deletePermission("1");

        ResponseEntity<Void> response = permissionController.deletePermission("1");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(permissionService, times(1)).deletePermission("1");
    }
}
