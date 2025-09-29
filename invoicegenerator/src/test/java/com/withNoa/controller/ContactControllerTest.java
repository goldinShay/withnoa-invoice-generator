package com.withNoa.controller;

import com.withNoa.entity.Contact;
import com.withNoa.entity.enums.ContactType;
import com.withNoa.entity.enums.Language;
import com.withNoa.repository.ContactRepository;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.io.ByteArrayInputStream;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContactRepository contactRepository;

    @WithMockUser
    @Test
    void whenExportContacts_thenExcelIsGeneratedWithCorrectHeaders() throws Exception {
        Contact contact = new Contact();
        contact.setFirstName("Noa");
        contact.setMiddleName("L.");
        contact.setLastName("Goldin");
        contact.setEmail("noa@withnoa.com");
        contact.setPhoneNumber("123456789");
        contact.setCompanyName("With Noa");
        contact.setContactType(ContactType.BUSINESS);
        contact.setPreferredLanguage(Language.ENGLISH);

        when(contactRepository.findAll()).thenReturn(Collections.singletonList(contact));

        byte[] responseBytes = mockMvc.perform(get("/contacts/export"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=contacts.xlsx"))
                .andReturn()
                .getResponse()
                .getContentAsByteArray();

        Sheet sheet = WorkbookFactory.create(new ByteArrayInputStream(responseBytes)).getSheet("Contacts");
        Row header = sheet.getRow(0);

        assert header.getCell(0).getStringCellValue().equals("First Name");
        assert sheet.getRow(1).getCell(0).getStringCellValue().equals("Noa");
    }

    @WithMockUser
    @Test
    void whenSaveContact_thenContactIsSavedAndRedirected() throws Exception {
        mockMvc.perform(post("/contacts/save")
                        .param("firstName", "Noa")
                        .param("lastName", "Goldin")
                        .param("email", "noa@withnoa.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/contacts"));

        verify(contactRepository, times(1)).save(any(Contact.class));
    }


    @WithMockUser
    @Test
    void whenListContacts_thenModelContainsContacts() throws Exception {
        when(contactRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/contacts"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("contacts"))
                .andExpect(view().name("contact-list"));
    }
}
