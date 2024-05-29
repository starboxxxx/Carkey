package carkey.project.notice.service;

import carkey.project.notice.domain.Notice;
import carkey.project.notice.dto.*;
import carkey.project.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository  noticeRepository;

    @Transactional
    public NoticeDto save(NoticeSaveDto noticeSaveDto) {
        Notice notice = new Notice(noticeSaveDto.getTitle(), noticeSaveDto.getComment());
        noticeRepository.save(notice);
        return NoticeDto.toDto(notice);
    }

    @Transactional
    public RecentNoticeDto findRecentNotice() {
        Notice notice = noticeRepository.findFirstByOrderByCreatedDateDesc();
        return RecentNoticeDto.toDto(notice);
    }

    @Transactional
    public List<NoticeListDto> findAll() {
        List<Notice> noticeDtos = noticeRepository.findAll();
        Collections.reverse(noticeDtos);
        List<NoticeListDto> noticeListDtos = new ArrayList<>();
        noticeDtos.forEach(s -> noticeListDtos.add(NoticeListDto.toDto(s)));
        return noticeListDtos;
    }

    @Transactional
    public NoticeDto findNotice(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId).get();
        return NoticeDto.toDto(notice);
    }

    @Transactional
    public AdminNoticeDto findAdminNotice(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId).get();
        return AdminNoticeDto.toDto(notice);
    }

    @Transactional
    public NoticeDto edit(Long noticeId, NoticeSaveDto noticeSaveDto) {
        Notice notice = noticeRepository.findById(noticeId).get();
        notice.setTitle(noticeSaveDto.getTitle());
        notice.setComment(noticeSaveDto.getComment());
        noticeRepository.save(notice);
        return NoticeDto.toDto(notice);
    }

    @Transactional
    public void delete(Long noticeId) {
        noticeRepository.deleteById(noticeId);
    }
}
