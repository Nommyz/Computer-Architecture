            lw  0   5   pos1   ; $5 = 1
            lw  0   1   32766      ; $1 = n
            lw  0   2   10833      ; $2 = r
            jalr 6   4           ; call combination; $4=return address; $3=answer
            halt

combi    sw  7   4   stack   ; save return address on stack
         add 7   5   7      ; increment stack pointer
         sw  7   1   stack   ; save $1 (n) on stack
         add 7   5   7      ; increment stack pointer
         sw  7   2   stack   ; save $2 (r) on stack
         beq 2   0   base   ; if r == 0, return 1
         beq 1   2   base   ; if n == r, return 1
         lw  0   6   neg1    ; $6 = -1
         add 7   6   7      ; decrement stack pointer
         lw  7   2   stack   ; load $2 (r) from stack
         add 7   6   7      ; decrement stack pointer
         lw  7   1   stack   ; load $1 (n) from stack
         add 7   6   7      ; decrement stack pointer
         lw  0   3   noop    ; $3 = 0 (initialize result)

loop    beq 1   2   done   ; if n == r, we're done
        beq 2   0   done   ; if r == 0, we're done
        add 3   3   1     ; result += n
        add 3   3   6     ; result--
        add 1   1   6     ; n--
        add 2   2   6     ; r--
        jalr 6   4          ; recursive call
        lw  0   2   noop   ; $2 = 0 (for the next subtraction)
        add 2   3   2     ; r = result
        lw  7   3   stack  ; load previous $3 (result) from stack
        add 7   6   7     ; decrement stack pointer
        jalr 4   2          ; return

base    lw  0   3   pos1   ; $3 = 1
        lw  7   2   stack  ; load $2 (r) from stack
        add 7   6   7     ; decrement stack pointer
        lw  7   1   stack  ; load $1 (n) from stack
        add 7   6   7     ; decrement stack pointer

done    lw  7   4   stack  ; load return address from stack
        jalr 4   2          ; return

noop    noop

pos1       .fill 1
neg1       .fill -1
stack      .fill 0
