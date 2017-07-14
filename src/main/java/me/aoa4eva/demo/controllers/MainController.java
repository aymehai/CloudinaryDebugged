package me.aoa4eva.demo.controllers;

import java.io.IOException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.utils.ObjectUtils;
import me.aoa4eva.demo.configuration.CloudinaryConfig;

@Controller
public class MainController {
	
	
	@Autowired
	CloudinaryConfig cloudc;
	
	
	@GetMapping("/upload")
	public String showUploadForm()
	{
		return "upload";
	}
	
	@PostMapping("/upload")
    public String singleImageUpload(@RequestParam("file") MultipartFile file, Model model){
        if (file.isEmpty()){
            model.addAttribute("message","Please select a file to upload");
            return "upload";
        }
        try {           
           //Map uploadResult =  cloudc.upload(file.getBytes(), ObjectUtils.asMap("public_id",file.getOriginalFilename()));
           
           //Uses an automatic naming scheme for the resource 
            Map uploadResult =  cloudc.upload(file.getBytes(), ObjectUtils.asMap("resource","auto"));

            //Gets the name of the file 
            String name = uploadResult.get("public_id").toString();
            System.out.println("File: "+name); 
            System.out.println(" ");
           //Gets the url of the uploaded file 
           String url = uploadResult.get("url").toString();
           System.out.println(url+" should display something meaningful");
           System.out.println(" ");
           
           String modified = cloudc.createUrl(name, 100, 150, "fit", "sepia");
           
           System.out.println("Cloudinary URL"+modified.toString());
           System.out.println(" ");
           model.addAttribute("message", "You successfully uploaded '" + file.getOriginalFilename() + "'");
           model.addAttribute("imageurl", uploadResult.get("url"));
           model.addAttribute("sizedimageurl", modified);
                  } 
        catch (IOException e){
            e.printStackTrace();
            model.addAttribute("message", "Sorry I can't upload that!");
        }
        return "upload";
	}
}
