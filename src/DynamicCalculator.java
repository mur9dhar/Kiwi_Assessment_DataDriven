import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;



public class DynamicCalculator {

	//@SuppressWarnings("unused")
	public static void main(String[] args) throws InterruptedException, IOException 
	{
		// TODO Auto-generated method stub
		System.out.println("Welcome To the Testing of Calculator");
		System.out.println("System.getProperty(\"user.dir\")" + System.getProperty("user.dir"));
		//Creating a Driver object for Chrome
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") +"\\lib\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();

		//invoking the URL
		driver.get("https://calculator-web.azurewebsites.net/");
		
		//displaying the Title of the URL
		String title = driver.getTitle();
		System.out.println(title);
		
		//Validating the URL hit so that the page has not redirected to any malicious website
		String curl = driver.getCurrentUrl();
		System.out.println(curl);
		if (curl.equals("https://calculator-web.azurewebsites.net/")) {
				System.out.println("Valid Webpage Launched");
		} else {
			
			System.out.println("Sorry! It's Not the Webpage You want to Browse");
			driver.close();
		}
		
		// Creating objects to read a xlsx file, workbook and sheet
		String resultSpreadSheet = System.getProperty("user.dir") + "//Kiwi_Cal_TS.xlsx";
		FileInputStream fis = new FileInputStream(resultSpreadSheet);
		//@SuppressWarnings("resource")
		@SuppressWarnings("resource")
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet sheet = workbook.getSheet("TestData");
		
		//FileOutputStream fos = new FileOutputStream("\"D://Kiwi_Cal_TS.xlsx\"");
		// iterator object to go through all the rows
		Iterator<Row> rows = sheet.iterator();
		rows.next();
		while(rows.hasNext()){
			Row r = rows.next();
						
			if (r.getCell(1) != null || r.getCell(2) != null || r.getCell(3) != null) {
				
				String ln = r.getCell(1).getStringCellValue().trim();
				String rn = r.getCell(3).getStringCellValue().trim();
				String op = r.getCell(2).getStringCellValue().trim();
			
				if ((ln != null && !ln.trim().isEmpty()) 
						|| (rn != null && !rn.trim().isEmpty()) 
						|| (op != null && !op.trim().isEmpty())) {
					
				
					System.out.print(ln);
					System.out.print(op);
					System.out.print(rn);
					
					driver.findElement(By.id("leftNumber")).sendKeys(ln);
				    driver.findElement(By.id("rightNumber")).sendKeys(rn);
					driver.findElement(By.id("operator")).sendKeys(op);
					
					driver.switchTo().frame(0);
					driver.findElement(By.xpath("//*[@id='calculate']")).click();
					Thread.sleep(1000);
					driver.switchTo().defaultContent();
		
					WebDriverWait wait = new WebDriverWait(driver,50);
					wait.until(ExpectedConditions.attributeToBeNotEmpty(driver.findElement(By.cssSelector("input[class=\"result\"]")), "value"));
			
					//System.out.println("Result = " + driver.findElement(By.cssSelector("input[class=\"result\"]")).getAttribute("value"));
					String res = driver.findElement(By.cssSelector("input[class=\"result\"]")).getAttribute("value");
					System.out.println("="+res);
					
					r.getCell(5).setCellValue(res.trim());
					driver.findElement(By.id("leftNumber")).clear();
					driver.findElement(By.id("rightNumber")).clear();
		
					FileOutputStream fos = new FileOutputStream(resultSpreadSheet);
				    workbook.write(fos);
				    fos.close();
				    
				    int a=0,b=0, resval;
				    a=Integer.parseInt(ln);
				    b=Integer.parseInt(rn);
				    if(op.equals("+"))
				    			{resval=a+b; }
				    else if(op.equals("-"))
				    			{resval=a-b;  }
				    else if(op.equals("*"))
				    			{resval=a*b;  }
				    else
				    	{
				    	if (b==0)
				    			{ resval=-1;  }
				    	else
				    			{resval=a/b;  }
				    	}
				    
				    System.out.println("resval="+resval);
				    
				if(resval==Integer.parseInt(res))
					r.getCell(6).setCellValue("Pass");
				else
					r.getCell(6).setCellValue("Fail");
				}
			}
		}
		
		fis.close();
		
	}
}
