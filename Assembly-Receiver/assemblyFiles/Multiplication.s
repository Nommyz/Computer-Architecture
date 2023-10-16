        lw      0       6       mcand       ;num1
        lw      0       2       mplier      ;num2
        lw      0       3       pos1        ;$3 = 1
        lw      0       4       exit        ;exit$4 = 20
loop1   nand    2       3       5
        nand    5       5       5           ;check bit
        beq     5       0       loop2       ;if 00 -> loop2
        add     1       6       1           ;else $1 <- $6
        halt                                ;end
loop2   add     6       6       6           ;shift mcand
        add     3       3       3           ;shift pos1
        lw      0       7       neg1        ;$7= -1
        add     4       7       4           ;exit-1
        beq     4       0       end         ;if 4=0 -> end(exitเหลือ0)
        beq     0       0       loop1       ;else loop1
        noop
end     halt
mcand   .fill   32766
mplier  .fill   10383
pos1    .fill   1
neg1    .fill   -1
exit    .fill   20
