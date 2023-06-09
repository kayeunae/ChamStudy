package ChamStudy.Service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ChamStudy.Dto.CompletionContentDto;
import ChamStudy.Dto.CompletionContentInterface;
import ChamStudy.Dto.CertificateDto;
import ChamStudy.Dto.CertificateInterface;
import ChamStudy.Dto.CompletionListDto;
import ChamStudy.Dto.MainFormDto;
import ChamStudy.Dto.MyClassLearningDto;
import ChamStudy.Dto.MyClassLearningSearchDto;
import ChamStudy.Dto.UserInfoDto;
import ChamStudy.Dto.UserSearchDto;
import ChamStudy.Entity.ApplyList;
import ChamStudy.Entity.ClassInfo;
import ChamStudy.Entity.Completion;
import ChamStudy.Entity.ContentInfo;
import ChamStudy.Entity.ContentVideo;
import ChamStudy.Entity.StudyHistory;
import ChamStudy.Entity.StudyResult;
import ChamStudy.Entity.UserInfo;
import ChamStudy.Repository.ApplyListRepository;
import ChamStudy.Repository.ClassInfoRepository;
import ChamStudy.Repository.CompletionRepository;
import ChamStudy.Repository.OnContentRepository;
import ChamStudy.Repository.OnContentVideoRepository;
import ChamStudy.Repository.StudyHistortRepository;
import ChamStudy.Repository.StudyResultRepository;
import ChamStudy.Repository.UserMainMypageRepository;
import ChamStudy.Repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserMainMyPageService {
	
	private final PasswordEncoder passwordEncoder;
	private final UserMainMypageRepository userMainMypageRepository;
	private final UserRepository userRepository;
	private final CompletionRepository completionRepository;
	private final ApplyListRepository applyListRepository;
	private final OnContentRepository onContentRepository;
	private final ClassInfoRepository classInfoRepository;
	private final StudyHistortRepository studyHistortRepository;
	private final OnContentVideoRepository contentVideoRepository;
	private final StudyResultRepository studyResultRepository;

	
	public UserInfoDto getUser(String email) throws Exception{
		
		UserInfo userInfo = userMainMypageRepository.findByEmail(email);
		
		UserInfoDto userInfoDto = new UserInfoDto();
		
		userInfoDto.setEmail(userInfo.getEmail());
		userInfoDto.setGubun(userInfo.getGubun());
		userInfoDto.setName(userInfo.getName());
		userInfoDto.setPhone(userInfo.getPhone());
		userInfoDto.setPassword(userInfo.getPassword());
		userInfoDto.setRegTime(userInfo.getRegDate());
		userInfoDto.setRole(userInfo.getRole());
		
		
		return userInfoDto;
	}
	
	public Long getUserId(String email) {
		Long userId = userMainMypageRepository.getUserId(email);
		return userId;
	}
	
	public Long updateUser(UserInfoDto userInfoDto) throws Exception{
		
		UserInfo userInfo = userRepository.findByemail(userInfoDto.getEmail());
		
		userInfo.updateUserMypage(userInfoDto, passwordEncoder);
		
		return userInfo.getId();
	
}
	public String getCategoryName() {
		String cateName = completionRepository.getCategoryName();
		return cateName;
	}
	
	@Transactional(readOnly = true)
	public Page<CompletionListDto> getCompletionList(UserSearchDto userSearchDto, CompletionListDto completionListDto, Pageable pageable, Long id){
		return completionRepository.getCompletionList(userSearchDto, completionListDto, pageable, id);
	}
	
	@Transactional(readOnly = true)
	public List<CompletionContentInterface> getVideo(Long contentId) {
		return completionRepository.getCompeltionContent(contentId);
	}
	
	@Transactional(readOnly = true)
	public CompletionContentInterface getVideoOne(Long contentId) {
		return completionRepository.getCompeltionContentOne(contentId);
	}
	
	@Transactional(readOnly = true)
	public CompletionContentInterface getVideoOther(Long contentId, Long videoId) {
		return completionRepository.getCompeltionContentOther(contentId, videoId);
	}
	
	@Transactional(readOnly = true)
	public Page<MyClassLearningDto> getLearningPage(MyClassLearningDto classLearningDto,Pageable pageable,MyClassLearningSearchDto classLearningSearchDto,String email){
		return applyListRepository.getLearningDto(classLearningDto, pageable,classLearningSearchDto,email);
	}
	
	@Transactional(readOnly = true)
	public CompletionContentInterface getLearningVideo1(Long classId) {
		return completionRepository.getApplyContentOne(classId);
	}
	
	@Transactional(readOnly = true)
	public List<CompletionContentInterface> getLearningVideo(Long contentId, Long classId, Long applyId) {
		return completionRepository.getApplyContent(contentId, classId, applyId);
	}
	
	@Transactional(readOnly = true)
	public CompletionContentInterface getLearningVideoOther(String email, Long videoId, Long classId) {
		return completionRepository.getLearningContentOther(email, videoId, classId);
	}
	
	public void createStudyHistory(Long contentId, String email,String flag, Long videoId, Long classId) {
		ContentVideo videoInfo = null;
		if (flag =="F") {
			videoInfo = contentVideoRepository.getVideoIdF(contentId);
		} else {
			videoInfo = contentVideoRepository.getVideoId(videoId);
		}
		ContentInfo ContentInfo = onContentRepository.getContentId(contentId);
		UserInfo userId = userRepository.getUserId(email);
		ClassInfo classInfo = classInfoRepository.getClassInfoConClass(contentId,classId);
		ApplyList applyList = applyListRepository.findByUserId(userId.getId(),classInfo.getId());
		Long history_id = studyHistortRepository.getVideoId(videoInfo.getId(),applyList.getId());
		StudyHistory studyHistory = new StudyHistory();
		if(history_id == null) {
			studyHistory.setVideoId(videoInfo);
			studyHistory.setContentId(ContentInfo);
			studyHistory.setApplyId(applyList);
		} else {
			studyHistory.setVideoId(videoInfo);
			studyHistory.setContentId(ContentInfo);
			studyHistory.setId(history_id);
			studyHistory.setApplyId(applyList);
		}
		studyHistortRepository.save(studyHistory);
	}
	
	public Long getVideoId(Long contentId) {
		return contentVideoRepository.getVideoIdF(contentId).getId();
	}
	
	public Long getfirstVideoId(Long contentId) {
		return contentVideoRepository.getfirstVideoId(contentId).getId();
	}
	public Long getLastVideoId(Long contentId) {
		return contentVideoRepository.getlastVideoId(contentId).getId();
	}
	
	
	public void createStudyResult(Long contentId, String email, Long classId) {
		UserInfo userId = userRepository.getUserId(email);
		ClassInfo classInfo = classInfoRepository.getClassInfoConClass(contentId,classId);
		ApplyList applyId = applyListRepository.findByUserId(userId.getId(),classInfo.getId());
		Long progress = studyHistortRepository.getProgress(applyId.getId(),contentId);
		StudyResult validResult = studyResultRepository.getResultId(applyId.getId());
		StudyResult studyResult = new StudyResult();
		 
		//study_result id가 중복할경우(=applyId가 중복확인)
		if(validResult != null) {
			studyResult.setId(validResult.getId());
		}
		studyResult.setApplyId(applyId);
		studyResult.setProgress(progress);
		studyResultRepository.save(studyResult);
		if(validResult != null) { //study_result에 값이 있는지?
			if(validResult.getProgress()>=100) { //study_result의 progress값이 100점인지
				Completion compleId = completionRepository.getCompletion(validResult.getId()); //수료테이블에 데이터가 있는지?
				if(compleId==null) {
					Completion completion = new Completion();
					completion.setResultId(validResult);
					completionRepository.save(completion);

					ApplyList applyInfo = new ApplyList();
					applyInfo.setId(applyId.getId());
					applyInfo.setClassInfo(classInfo);
					applyInfo.setUserInfo(userId);
					applyInfo.setComFlag("Y");
					applyListRepository.save(applyInfo);
				}
			}
		}
	}
	
	public Page<CertificateDto> getCompletionList(CertificateDto certificateDto,Pageable pageable, String email){
		return completionRepository.getCompletionPage(certificateDto, pageable, email);
	}
	
	public CertificateInterface getCertificateInfo(Long compId) {
		return completionRepository.getCertificateInfo(compId);
	}
	
	public Long getApplyId(Long classId, String email) {
		Long userId = userRepository.getUserId(email).getId();
		Long applyId = applyListRepository.findByClassInfoIdAndUserInfoId(classId, userId).getId();
		return applyId;
	}
	
}
