package cfp.wecare.util;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
@Service
public class ExcelUtil {

    public boolean isTypeExcel(MultipartFile multipartFile){
        return multipartFile.getContentType() == Constants.TYPE;
    }
}
