package io.debc.nft.thread;

import com.esaulpaugh.headlong.abi.Tuple;
import com.esaulpaugh.headlong.abi.TupleType;
import com.esaulpaugh.headlong.util.FastHex;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Iterator;

/**
 * @description:
 * @author: Jalivv
 * @create: 2022-12-12 13:25
 **/
public class TestMain {

    public static void main(String[] args) throws Exception {
        String s = "0x0000000000000000000000000000000000000000000000000000000000000040000000000000000000000000000000000000000000000000000000000000008000000000000000000000000000000000000000000000000000000000000000016123d4abd919f6c17a218546a5b35b15a7fb354b0000000000000000020010010000000000000000000000000000000000000000000000000000000000000001000000000000000000000000000000000000000000000000000000000000000a";

        Tuple tuple = decode(s);
        BigInteger[] bigIntegers = tuple.get(0);
        Arrays.stream(bigIntegers).forEach(System.out::println);
        System.out.println();
    }

    private static Tuple decode(String s) {
        TupleType tupleType = TupleType.parse("(uint256[],uint256[])");
        Tuple tuple = tupleType.decode(FastHex.decode(s.substring(2)));
        return tuple;
    }


}
