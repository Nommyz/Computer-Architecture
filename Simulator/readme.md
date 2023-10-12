2/10/2566
- เพิ่มโครงร่างของ simulator
    - todo-list
        - case แต่ละแบบ เช่น add 000, nand 001
        - typeต่างๆ ที่ต้องใช้ เช่น r-format i-format j-format
        - Error Handling ยังไม่ได้ทำ
- BUG
    - Error ในด้านตัวแปร และ logic


4/10/2566
- เพิ่มโครงร่างของ simulator
    - todo-list
        - case แต่ละแบบ
        - typeต่างๆ ที่ต้องใช้ เช่น r-format i-format j-format
        - loop และ logic ต่างๆ
- สิ่งที่แก้ไข
    - fixed bug
        - แก้ไข bug ตัวแปร
    - เพิ่มการอ่านไฟล์ machine-code.txt
    - เพิ่มไฟล์ machine-code.txt

7/10/2566
- แก้ println ใน printstate ให้เป็นแบบ Java (ตอนแรกเป็นแบบ C++)
- เขียนเพิ่มใน switch-case
      - nand ยังไม่เสร็จ
      - lw, sw, beq, jalr, halt เขียนแล้ว(แก้ไขได้นะ)
- เขียนเพิ่มใน IFormat, JFormat, OFormat

8/10/2566
- แก้ตรง ishalt ตอนแรกเป็น (0 / 1) -> (true / false)
- แก้ตรงฟังก์ชัน convertNum ที่ error (ไม่ใช้ฟังก์ชัน convertNum แล้ว แต่เขียนให้ OffsetField ทำ 2s' complement ในฟังก์ชัน IFormat เลย)

8/10/2566
- เขียนเพิ่มใน switch-case ส่วน case 1: //nand 001

9/10/2566
- fixed bug
- ทำให้ code เริ่มรันได้

12/10/2566
- สรุปแล้วที่รันครั้งก่อนยังเละๆ เพราะเขียนผิดในส่วน switch-case
- แก้ใน switch-case ตอนนี้รันได้เหมือนใน Example-Run Simulator.txt แล้วเกือบ 100%
- ยังขาดแค่ส่วนของ halt
