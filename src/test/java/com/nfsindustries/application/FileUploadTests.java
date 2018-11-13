package com.nfsindustries.application;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class FileUploadTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private StorageService storageService;

    @Test
    public void shouldSaveUploadedFile() throws Exception {
        ClassPathResource resource = new ClassPathResource("testupload.xml", getClass());
        String fileString = IOUtils.toString(resource.getInputStream(), "UTF-8");
        MockMultipartFile multipartFile = new MockMultipartFile("file", "testupload.xml",
                "text/plain", fileString.getBytes());
        this.mvc.perform(fileUpload("/getbalance/").file(multipartFile))
                .andExpect(status().isOk());
        then(this.storageService).should().store(multipartFile);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void should404WhenMissingFile() throws Exception {
        given(this.storageService.loadAsResource("testupload.xml"))
                .willThrow(StorageFileNotFoundException.class);

        this.mvc.perform(get("/files/testupload.xml")).andExpect(status().isNotFound());
    }

}
