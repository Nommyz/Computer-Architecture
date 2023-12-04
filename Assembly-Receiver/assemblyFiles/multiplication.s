        lw      0       6       mcand
        lw      0       2       mplier
        lw      0       3       pos1
        lw      0       4       exit
loop1   nand    2       3       5
        nand    5       5       5
        beq     5       0       loop2
        add     1       6       1
loop2   add     6       6       6
        add     3       3       3
        lw      0       7       neg1
        add     4       7       4
        beq     4       0       end
        beq     0       0       loop1
        noop
end     halt
mcand   .fill   3
mplier  .fill   2
pos1    .fill   1
neg1    .fill   -1
exit    .fill   15