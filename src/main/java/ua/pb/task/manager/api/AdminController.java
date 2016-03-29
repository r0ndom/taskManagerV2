package ua.pb.task.manager.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.pb.task.manager.aspect.annotation.AdminAccess;

/**
 * Created by Mednikov on 29.03.2016.
 */
@Controller
@AdminAccess
@RequestMapping("/admin")
public class AdminController {

}
