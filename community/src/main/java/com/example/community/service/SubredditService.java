package com.example.community.service;

import com.example.community.dto.SubredditDto;
import com.example.community.exceptions.SpringRedditException;
import com.example.community.mappers.SubredditMapper;
//import com.example.community.mappers.SubredditMapperImpl;
import com.example.community.model.Subreddit;
import com.example.community.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {
    private final SubredditRepository subredditRepository;
    private final SubredditMapper subredditMapper;

//    Subreddit subreddit;
    @Transactional
    public SubredditDto save(SubredditDto subredditDto){
        Subreddit save =subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto));
        subredditDto.setId(save.getId());
        return subredditDto;
    }


    @Transactional()
    public List<SubredditDto> getAll() {
        return subredditRepository.findAll().stream().map(subredditMapper:: mapSubredditToDto)
                .collect(toList());
    }


    public SubredditDto getSubreddit(Long id) {
        Subreddit subreddit = subredditRepository.findById(id).
                orElseThrow(()-> new SpringRedditException("No Subreddit found for id " + id));
        return subredditMapper.mapSubredditToDto(subreddit);
    }
}
