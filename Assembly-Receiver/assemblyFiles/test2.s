lw 0 1 one (Loads 1 into r1) r1 = 1
add 1 1 1 (r1 = r1+r1) r1 = 1+1
add 1 1 1 (r1 = r1+r1) r1 = 2+2
add 1 1 1 (r1 = r1+r1) r1 = 4+4
add 1 1 1 (r1 = r1+r1) r1 = 8+8
add 1 1 1 (r1 = r1+r1) r1 = 16+16
sw 0 1 store
done   halt

one .fill 1
two .fill 2
three .fill 3
four .fill 4
five .fill 5
six .fill 6
seven .fill 7
eight .fill 8
nine .fill 9
ten .fill 10

store noop