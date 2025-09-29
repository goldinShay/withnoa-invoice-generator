package com.withNoa.controller;

import com.lowagie.text.Row;
import com.withNoa.entity.Contact;
import com.withNoa.repository.ContactRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.List;


@Controller
@RequestMapping("/contacts")
public class ContactController {

    @Autowired
    private ContactRepository contactRepository;

    @GetMapping
    public String listContacts(Model model) {
        model.addAttribute("contacts", contactRepository.findAll());
        return "contact-list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("contact", new Contact()); // 👈 This line is essential
        return "contact-form";
    }

    @PostMapping("/save")
    public String saveContact(@ModelAttribute Contact contact) {
        contactRepository.save(contact);
        return "redirect:/contacts";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("contact", contactRepository.findById(id).orElseThrow());
        return "contact-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteContact(@PathVariable Long id) {
        contactRepository.deleteById(id);
        return "redirect:/contacts";
    }
    @GetMapping("/export")
    public void exportContactsToExcel(HttpServletResponse response) throws IOException {
        List<Contact> contacts = contactRepository.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Contacts");

        // Header row
        org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
        String[] columns = {"First Name", "Middle Name", "Last Name", "Email", "Phone", "Company", "Type", "Language"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(columns[i]);
        }

        // Data rows
        for (int i = 0; i < contacts.size(); i++) {
            Contact c = contacts.get(i);
            org.apache.poi.ss.usermodel.Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(c.getFirstName());
            row.createCell(1).setCellValue(c.getMiddleName());
            row.createCell(2).setCellValue(c.getLastName());
            row.createCell(3).setCellValue(c.getEmail());
            row.createCell(4).setCellValue(c.getPhoneNumber());
            row.createCell(5).setCellValue(c.getCompanyName());
            row.createCell(6).setCellValue(c.getContactType().toString());
            row.createCell(7).setCellValue(c.getPreferredLanguage().toString());
        }

        // Response setup
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=contacts.xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();
    }

}
