lw  0   1   num1    ;   num1 = เริ่ม
lw  0   2   num2    ;   num2 = จบ
lw  0   6   one     ;   $6 = 1
loop        beq 4   2   done    ;
            add 6   4   4       ;   $4 = $4+1
            add 4   3   3       ;   $3 = $3+$4
            beq 0   0   loop    ;
done    halt

one     .fill   1
num1    .fill   1
num2    .fill   100