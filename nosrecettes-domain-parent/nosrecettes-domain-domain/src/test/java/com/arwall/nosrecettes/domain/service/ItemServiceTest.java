package com.arwall.nosrecettes.domain.service;

import static com.arwall.nosrecettes.domain.stubfactories.ItemStubs.AN_ITEM_ID;
import static com.arwall.nosrecettes.domain.stubfactories.ItemStubs.AN_ITEM;
import static com.arwall.nosrecettes.domain.stubfactories.ItemStubs.AN_ITEM_NAME;
import static com.arwall.nosrecettes.domain.stubfactories.ItemStubs.A_NEW_ITEM_ID;
import static com.arwall.nosrecettes.domain.stubfactories.ItemStubs.A_NEW_ITEM;
import static com.arwall.nosrecettes.domain.stubfactories.ItemStubs.A_QUANTITY_TYPE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import com.arwall.nosrecettes.domain.model.Item;
import com.arwall.nosrecettes.domain.gatesToInfra.spi.PersistItem;

class ItemServiceTest {

    ItemService fixture;

    PersistItem itemPersist;

    @BeforeEach
    void setup() {
        itemPersist = Mockito.mock(PersistItem.class);
        fixture = new ItemService(itemPersist);
    }

    @Test
    void should_retrieve_item() {
        // Given
        Long id = AN_ITEM_ID;
        Mockito.when(itemPersist.findById(id)).thenReturn(Optional.of(AN_ITEM));

        // When
        var actual = fixture.retrieve(id);

        // Then
        assertThat(actual).isEqualTo(AN_ITEM);
    }

    @Test
    void should_delete_item() {
        // Given // When
        fixture.delete(AN_ITEM_ID);

        // Then
        Mockito.verify(itemPersist).delete(AN_ITEM_ID);
    }

    @Test
    void should_update_already_existed_item() {
        // Given // When
        var actual = fixture.update(AN_ITEM);

        // Then
        Mockito.verify(itemPersist).update(AN_ITEM);
    }

    @Test
    void should_create_and_save_when_update_new_item() {
        // Given
        var itemWithoutId = Item.builder()
                .withName(AN_ITEM_NAME)
                .withQuantityType(A_QUANTITY_TYPE)
                .build();
        var expected = Item.builder()
                .withQuantityType(A_QUANTITY_TYPE)
                .withName(AN_ITEM_NAME)
                .withId(A_NEW_ITEM_ID)
                .build();
        Mockito.when(itemPersist.create()).thenReturn(A_NEW_ITEM);
        Mockito.when(itemPersist.update(ArgumentMatchers.any()))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0, Item.class));

        // When
        var actual = fixture.update(itemWithoutId);

        // Then
        assertThat(actual.isTheSameEntityAs(expected)).isTrue();
    }

    @Test
    void should_find_all_items() {
        // Given
        List<Item> allItems = List.of(AN_ITEM);
        Mockito.when(itemPersist.findAll()).thenReturn(allItems);

        // When
        var actual = fixture.findAll();

        // Then
        Mockito.verify(itemPersist).findAll();
        assertThat(actual).isEqualTo(allItems);
    }
}