package com.app.services;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class SortService {
    public Pageable getSorter(int _page,int _size,String[] _sort,String[] _order) {
        if(_page < 1 || _size <= 0)
            return null;
        Sort sort;
        if(_sort != null && _order != null && _sort.length == _order.length && _sort.length > 0){
            sort = Sort.by(_order[0].toLowerCase().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC ,_sort[0]);
            for (int i = 1; i < _sort.length; i++) {
                sort.and(Sort.by(_order[i].toLowerCase().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC ,_sort[i]));
            }
        }else{
            sort = Sort.unsorted();
        }
        return PageRequest.of(_page - 1, _size,sort);
    }
}
