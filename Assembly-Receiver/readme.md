27/09/2566
- file reader ใช้อ่าน assembly จาก file อื่นๆ เช่น .s  
- tokenizer ใช้อ่านแต่ละตัวของ assembly บรรทัดนั้น

28/09/2566
- สร้างไฟล์ Assembler
- Assembler file update
  - สร้าง Machinecode map
  - สร้าง method convert decimal(int) to binary(string)
 - สร้าง test file

2/10/2566
  - เพิ่ม method toDecimal,twosCompliment ใน Assembler file
  - เพิ่ม test file เพื่อ test method toDecimal,twosCompliment
  - สร้าง method อื่นๆ ที่ต้องนำมาใช้ใน method toDecimal,twosCompliment เช่น flip และ toInteger (อาจนำไปใช้ที่อื่นๆด้วย)

6/10/2566
  - สร้าง method สำหรับแปลง assembly code เป็น machine code ทั้งในรูปแบบ decimal และ binary (Main zone in assembler file)
  - สร้าง method สำหรับ check ค่าและเงื่อนไขต่างๆ (Checking zone ใน assembler file)
  - แบ่ง zone ใน assembler file เพื่อให้อ่านง่าย ได้แก่
    - main zone
    - checking zone
    - binary operation zone
    - system zone(exit code)
  - สร้าง test case ง่ายๆ และ test case ที่อยู่ใน website project
  - เพิ่ม test method ใน test file
  - สร้าง Main file ใช้สำหรับ compile assembler เพื่อแปลง assembly code เป็น Machine code(ทั้ง decimal และ binary) 
  - Assembler code (.s) ในโฟลเดอร์ assemblyFiles
  - Machine code แต่ละแบบ  ในโฟลเดอร์ machineCodes
      - .bin(binary)
      - .txt(decimal)

13/10/2565
  - upgrade code และ algorithm เพื่อให้กระบวนการทำงานเร็วขึ้นประมาณ 14.957%
  - แก้ไขการคำนวณ Machine code ใน i-type ให้ถูกต้อง
