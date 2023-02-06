package huffman.node;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
class HuffmanNodeTest {
    private final HuffmanNode leafNode1 = new HuffmanNode(78, 66, null, null);
    private final HuffmanNode leafNode2 = new HuffmanNode(101, 4, null, null);

    private final HuffmanNode node = new HuffmanNode(65, 3, leafNode1, leafNode2);

    @Test
    void whenGetFrequencyIsCalled_ThenFrequencyShouldMatch() {
        int nodeFreq = node.getFrequency();
        Assert.assertEquals(nodeFreq, 3);
        Assert.assertEquals(leafNode2.getFrequency(), 4);
        Assert.assertEquals(leafNode1.getFrequency(), 66);
    }

    @Test
    void whenGetSymbolIsCalled_ThenSymbolShouldMatch() {
        int nodeSymbol = node.getSymbol();
        Assert.assertEquals(nodeSymbol, 65);
        Assert.assertEquals(leafNode1.getSymbol(), 78);
        Assert.assertEquals(leafNode2.getSymbol(), 101);
    }

    @Test
    void whenIsLeafNodeIsCalled_ThenLeafNodeShouldReturnTrue() {
        Assert.assertFalse(node.isLeafNode());
        Assert.assertTrue(leafNode1.isLeafNode());
        Assert.assertTrue(leafNode2.isLeafNode());
    }
}