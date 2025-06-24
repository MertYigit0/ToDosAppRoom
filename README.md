# Overview

Bu proje, kullanıcıların yapılacaklar listesini (To-Do) kolayca oluşturup yönetebileceği, modern Android mimarisiyle geliştirilmiş bir uygulamadır. Room veritabanı ile kalıcı veri saklama, MVVM mimarisi ile sürdürülebilir ve test edilebilir kod yapısı hedeflenmiştir. Kullanıcılar görev ekleyebilir, güncelleyebilir ve silebilir.

</br>

## :camera_flash: Screens :

<table>
<tr>
<th>Ana Liste Ekranı</th>
<th>Görev Ekleme Ekranı</th>
<th>Güncelleme Ekranı</th>
</tr>
<tr>
<td><img src="https://github.com/user-attachments/assets/da51c0d2-c856-4137-bcf9-6dd27f2dba55" height="500"></td>
<td><img src="https://github.com/user-attachments/assets/0301e4ee-d892-4cd7-a1b1-44c8189072cf" height="500"></td>
<td><img src="https://github.com/user-attachments/assets/ef7c3fae-932d-4108-96b8-8a3f80a8a21b" height="500"></td>
</tr>
</table>

<!-- Daha fazla ekran eklemek için yukarıdaki tabloyu çoğaltabilirsiniz -->

</br>

# Features

- Yapılacaklar listesine yeni görev ekleme  
- Mevcut görevleri güncelleme ve silme  
- Tüm görevleri listeleme  
- Modern ve kullanıcı dostu arayüz  
- Room ile kalıcı veri saklama  
- MVVM mimarisi ile modüler ve test edilebilir yapı  

# Technologies and Libraries

## Core Technologies

- Kotlin  
- Android Jetpack (ViewModel, LiveData, Navigation)  
- Room Database  
- MVVM Architecture  

## Libraries

- androidx.core: Temel Android uzantıları  
- androidx.appcompat: Geriye dönük uyumluluk  
- Material Components: Modern UI bileşenleri  
- Room: Yerel veritabanı yönetimi  
- RecyclerView: Listeleme işlemleri  
- ConstraintLayout: Esnek arayüz tasarımı  
- Fragment KTX: Fragment işlemleri için Kotlin uzantıları  

## :gear: Library Versions :

| Library           | Version  |
|-------------------|----------|
| Kotlin            | 2.0.21   |
| Room              | 2.6.1    |
| AppCompat         | 1.7.1    |
| Material          | 1.12.0   |
| RecyclerView      | 1.3.2    |
| ConstraintLayout  | 2.2.1    |
| Activity          | 1.10.1   |
| Fragment          | 1.7.0    |
| Lifecycle (ViewModel) | 2.8.0 |
| Core KTX          | 1.16.0   |
| JUnit             | 4.13.2   |
| Espresso Core     | 3.6.1    |

# Project Structure

- **data/**: Room veritabanı, DAO ve veri modeli burada bulunur.  
- **repository/**: Veri kaynakları ile ViewModel arasındaki köprü.  
- **ui/**: Fragmentler ve RecyclerView adaptörü.  
- **viewmodel/**: ViewModel ve Factory sınıfları.  
- **res/**: Uygulamanın arayüz ve kaynak dosyaları.  

---

