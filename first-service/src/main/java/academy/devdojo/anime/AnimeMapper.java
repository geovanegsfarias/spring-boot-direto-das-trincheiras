package academy.devdojo.anime;

import academy.devdojo.domain.Anime;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AnimeMapper {

    Anime toAnime(AnimePostRequest request);

    Anime toAnime(AnimePutRequest request);

    AnimeGetResponse toAnimeGetResponse(Anime anime);

    List<AnimeGetResponse> toAnimeGetResponseList(List<Anime> animes);

    AnimePostResponse toAnimePostResponse(Anime anime);
}