        lw  0   1   n 			;register1 = n
        lw  0   2   r 			;register2 = r
        lw  0   6   stack 		;register6 = stack
        lw  0   7   rAddr 		;register7 = rAddr (return address)
        
combi   beq 1   2 combi2 		;check if n = r go to combi2
        beq 2   0 combi2 ;check (else) if r = 0 go to combi2
        lw  0   5   neg1 		;register5 = neg1(temporary value)
        add 1   5   1 			;n = n + (-1)
        lw  0   5   pos1 		;register5 = pos1
        sw  6   7   stack 		;save return address(rad) on stack
        add 6   5   6 			;stack = pos1 + stack
        sw  6   1   stack 		;save n on stack
        add 6   5   6 			;stack = pos1 + stack
        sw  6   2   stack 		;save r on stack
        add 6   5   6 			;increment stack pointer
        lw  0   5   comAdr 		;register5 = comAdr
        jalr 5  7 			;call combi; register5=comAdr address; register6=return address

        add 3   0   4 			;register4 = register3 + 0
        lw  0   5   pos1 		;reset register5 = pos1
        sw  6   4   stack 		;save register6 on stack
        lw  0   5   neg1 		;register7 = neg1(temporary value)
        add 6   5   6 			;decrement stack pointer
        lw  6   2   stack 		;load r in stack
        add 6   5   6 			;decrement stack pointer
        lw  6   1   stack 		;load n in satck
        add 2   5   2 			;r = r - 1
        lw  0   5   pos1 		;reset register7 = pos1
        add 6   5   6 			;increment stack pointer
        add 6   5   6 			;increment stack pointer
        add 6   5   6   			;increment stack pointer
        lw  0   5   comAdr 		;register7 = comAdr
        jalr 5  7 				;call combi; register6=return address

        lw  0   5   neg1 		;register5 = neg1
        add 6   5   6 			;decrease stack pointer
        lw  6   4   stack 		;load register4 on stack
        add 3   4   3 			;register3 = register3 + register4
        add 6   5   6 			;decrement stack pointer
        add 6   5   6 			;decrement stack pointer
        add 6   5   6 			;decrement stack pointer
        lw  6   7   stack 			;load rad(return address) on stack
        jalr 7  5 				;call return; register7=end halt address
        beq 0   0   end          ;jump to end

combi2 lw  0   5   pos1  		;register7 = pos1 (reset register7)
        add 0   5   3 			;register3 = pos1 + 0
        jalr    7   5 			;return.  register7 is not restored
 
end halt 				;end

n       .fill   7 
r       .fill   3 
pos1    .fill    1 
neg1    .fill   -1 
comAdr     .fill   combi 
rAddr     .fill   end 
stack   .fill   0                ;beginning of stack (value is irrelevant)