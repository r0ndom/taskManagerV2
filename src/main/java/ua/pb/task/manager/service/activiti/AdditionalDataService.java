package ua.pb.task.manager.service.activiti;

import org.activiti.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.pb.task.manager.repository.UserRepository;
import ua.pb.task.manager.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by Mednikov on 22.02.2016.
 */
@Service("additionalInfo")
public class AdditionalDataService {

    @Autowired
    private UserRepository userDao;
    @Autowired
    private ActivitiService service;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private RequestUtil requestUtil;
    @Autowired
    private HttpServletRequest request;

    public String getStartDate(String execId) {
        String id = service.getTaskIdByExecutionId(execId);
        Map<String, Object> map = service.getParamsByTaskId(id);
        for (String key : map.keySet()) {
            if (key.equals("startDate"))
                return (String) map.get(key);
        }
        return getFormattedDate(new Date());
    }

    public String getEndDate() {
        return getFormattedDate(new Date());
    }

    public String getUser() {
        return userDao.find(requestUtil.getUserSession(request).getId()).getMainEmail();
    }

    public Map<String, String> getUsers() {
        return userDao.findAllMap();
    }

    public void defineExecutor(String execId) {
        String id = service.getTaskIdByExecutionId(execId);
        Map<String, Object> map = service.getParamsByTaskId(id);
        String developer = null;
        for (String key : map.keySet()) {
            if (key.equals("developer"))
                developer = (String) map.get(key);
        }
        if (developer != null)
            runtimeService.setVariable(execId, "executor", developer);
    }

    public String getElapsedTime(String start, String end) {
        Date s = parseMuraviewDate(start);
        Date e = parseMuraviewDate(end);
        long diff = e.getTime() - s.getTime();

        long diffMinutes = diff / (60 * 1000) % 60;

        return String.valueOf(diffMinutes);
    }

    public Date parseMuraviewDate(String string) {
        try {
            SimpleDateFormat murviewsdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            return murviewsdf.parse(string);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getFormattedDate(Date date) {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        return df.format(date);
    }

}
