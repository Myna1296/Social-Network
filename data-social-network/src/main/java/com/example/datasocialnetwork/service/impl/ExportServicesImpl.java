package com.example.datasocialnetwork.service.impl;

import com.example.datasocialnetwork.common.Constants;
import com.example.datasocialnetwork.config.UserAuthDetails;
import com.example.datasocialnetwork.dto.response.ResponseOk;
import com.example.datasocialnetwork.entity.User;
import com.example.datasocialnetwork.repository.*;
import com.example.datasocialnetwork.service.ExportService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ExportServicesImpl implements ExportService {

    @Autowired
    private FriendShipRepository friendShipRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikeRepository likeRepository;


    @Override
    public ResponseEntity<?> exportFile() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserAuthDetails userDetails = (UserAuthDetails) authentication.getPrincipal();
            User user = userRepository.findOneByUserName(userDetails.getUsername());
            if (user == null) {
                ResponseOk response = new ResponseOk();
                response.setCode(Constants.CODE_ERROR);
                response.setMessage(Constants.MESS_013);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            // Lấy ngày 7 ngày trước đó

            long totalFriend7Day = friendShipRepository.countAcceptedFriendshipsAndCreatedDateAfter(user.getId(), LocalDateTime.now().minusDays(7));
            long totalStatus7Day = statusRepository.countStatusTAndCreatedDateAfter(user.getId(), LocalDateTime.now().minusDays(7));
            long totalUserComment7Day = commentRepository.countCommentsByUserIdAndCreatedDateAfter(user.getId(), LocalDateTime.now().minusDays(7));
            long totalCommentByStatusUser7Day = commentRepository.countCommentsByStatusUserIdAndCreatedDateAfter(user.getId(), LocalDateTime.now().minusDays(7));
            long totalUserLike7Day = likeRepository.countLikesByUserIdAndCreatedDateAfter(user.getId(), LocalDateTime.now().minusDays(7));
            long totalLikeStatusUSer7Day = likeRepository.countLikesByStatusUserIdAndCreatedDateAfter(user.getId(), LocalDateTime.now().minusDays(7));
            // Tạo một workbook mới
            Workbook workbook = new XSSFWorkbook();
            // Định dạng ngày
            DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyyMMdd");
            String formattedCurrentDate = LocalDate.now().format(formatterDate);
            String formattedSevenDaysAgo = LocalDate.now().minusDays(7).format(formatterDate);
            // Tạo một trang trong workbook
            Sheet sheet = workbook.createSheet("Bao cao " + formattedSevenDaysAgo + "~" + formattedCurrentDate);

            // Tạo dòng đầu tiên (header)
            Row headerRow = sheet.createRow(0);

            // Tạo ô header và ghi giá trị cho từng ô
            Cell headerCell1 = headerRow.createCell(0);
            headerCell1.setCellValue("Total new friend");

            Cell headerCell2 = headerRow.createCell(1);
            headerCell2.setCellValue("My total post");

            Cell headerCell3 = headerRow.createCell(2);
            headerCell3.setCellValue("My total comment");

            Cell headerCell4 = headerRow.createCell(3);
            headerCell4.setCellValue("Total number of comments on my posts");

            Cell headerCell5 = headerRow.createCell(4);
            headerCell5.setCellValue("My total like");

            Cell headerCell6 = headerRow.createCell(5);
            headerCell6.setCellValue("Total number of like on my posts");
            // Ghi dữ liệu vào sheet
            int rowNum = 1;
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(totalFriend7Day);
            row.createCell(1).setCellValue(totalStatus7Day);
            row.createCell(2).setCellValue(totalUserComment7Day);
            row.createCell(3).setCellValue(totalCommentByStatusUser7Day);
            row.createCell(4).setCellValue(totalUserLike7Day);
            row.createCell(5).setCellValue(totalLikeStatusUSer7Day);

            // Đường dẫn để lưu file Excel trên ổ D
            DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            String formattedDateTime = LocalDateTime.now().format(formatterDateTime);
            String filePath = "D:/" + "Bao cao " + formattedDateTime + ".xlsx";

            // Lưu workbook vào file trên ổ D
            FileOutputStream fileOut = new FileOutputStream(filePath);
            workbook.write(fileOut);
            workbook.close();

            ResponseOk response = new ResponseOk();
            response.setCode(Constants.CODE_OK);
            response.setMessage("");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e) {
            ResponseOk response = new ResponseOk();
            response.setCode(Constants.CODE_ERROR);
            response.setMessage("Error while exporting report file");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }
}

