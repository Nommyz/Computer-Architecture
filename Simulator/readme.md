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
