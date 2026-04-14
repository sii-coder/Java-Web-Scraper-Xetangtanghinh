package com.trang;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // ==================== KHU VỰC "MẶT TIỀN" ====================
        String linkMotChuongBatKy = "...";
        int chuongBatDau = 1;
        int chuongKetThuc = 47;
        String tenFileLuu = "BanBackupTruyen_Moi.txt";
        // ============================================================

        // --- NÂNG CẤP XE TĂNG TÀNG HÌNH CHỐNG CLOUDFLARE ---
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("excludeSwitches", new String[] { "enable-automation" });
        options.setExperimentalOption("useAutomationExtension", false);
        options.addArguments("--disable-blink-features=AutomationControlled");
        // Giả danh trình duyệt Chrome bản 146
        options.addArguments(
                "lÊN TRÌNH DUYỆT BẤT KỲ GÕ my user agent COPY VÀO ĐÂY");

        WebDriver driver = new ChromeDriver(options);
        List<String> danhSachLink = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("BƯỚC 0: MỞ CỬA CHO SẾP VÀO ĐĂNG NHẬP...");

            // Chạy ra trang chủ trước
            driver.get("https://tytnovel.org/");

            // Báo động chờ ní thao tác
            System.out.println("\n=======================================================");
            System.out.println("🛑 KHOAN ĐÃ BẠN ƠI!");
            System.out.println("1. Hãy qua cái cửa sổ Chrome vừa hiện lên.");
            System.out.println("2. Đăng nhập vào tài khoản của bạn đi (bấm xác nhận mỏi tay thì ráng xíu nha).");
            System.out.println("3. Đăng nhập thành công xong, thử bấm vào 1 truyện xem đọc được chưa.");
            System.out.println(
                    "👉 4. Xong xuôi thì nhấp chuột vào cái Terminal này rồi BẤM PHÍM ENTER để tool chạy tiếp!");
            System.out.println("=======================================================\n");

            // Chờ ní bấm Enter
            scanner.nextLine();

            System.out.println("✅ Sếp đã đăng nhập xong! Tiến hành càn quét...");

            // Phi vào link truyện
            driver.get(linkMotChuongBatKy);
            Thread.sleep(3000);

            // Lấy danh sách link
            List<WebElement> cacOption = driver.findElements(By.cssSelector("select.form-select option"));
            for (WebElement opt : cacOption) {
                String link = opt.getAttribute("value");
                if (link != null && !link.isEmpty()) {
                    danhSachLink.add(link);
                }
            }
            System.out.println("📊 Tổng cộng hốt được: " + danhSachLink.size() + " link chương.");

            System.out.println("\n🚚 BƯỚC 2: BẮT ĐẦU 'KHUÂN' NỘI DUNG...");
            FileWriter writer = new FileWriter(tenFileLuu, StandardCharsets.UTF_8, true);

            for (int i = chuongBatDau - 1; i < chuongKetThuc; i++) {
                if (i >= danhSachLink.size())
                    break;

                String linkChuong = danhSachLink.get(i);
                int soThuTu = i + 1;

                driver.get(linkChuong);
                Thread.sleep(3000);

                String tenChuongThucTe = "CHƯƠNG " + soThuTu;
                List<WebElement> cacTheA = driver.findElements(By.cssSelector("div.vstack a"));
                if (cacTheA.size() >= 2) {
                    tenChuongThucTe = cacTheA.get(1).getText();
                }

                List<WebElement> paragraphs = driver.findElements(By.cssSelector("#chapterContent p"));
                if (!paragraphs.isEmpty()) {
                    writer.write(tenChuongThucTe + "\n");
                    for (WebElement p : paragraphs) {
                        writer.write(p.getText() + "\n");
                    }
                    writer.write("\n\n");
                    System.out.println("✅ Đã hốt xong: " + tenChuongThucTe);
                } else {
                    System.out.println("⚠️ Lỗi ở chương " + soThuTu + " (Không thấy nội dung)");
                }
            }
            writer.close();
            System.out.println("\n🎉 HOÀN THÀNH XUẤT SẮC! CHECK FILE ĐI BẠN.");

        } catch (Exception e) {
            System.out.println("❌ Có lỗi xảy ra trong lúc chạy:");
            e.printStackTrace();
        } finally {
            scanner.close();
            driver.quit();
        }
    }
}