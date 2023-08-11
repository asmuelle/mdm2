import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { PropertyTypeService } from '../service/property-type.service';

import { PropertyTypeComponent } from './property-type.component';

describe('PropertyType Management Component', () => {
  let comp: PropertyTypeComponent;
  let fixture: ComponentFixture<PropertyTypeComponent>;
  let service: PropertyTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'property-type', component: PropertyTypeComponent }]),
        HttpClientTestingModule,
        PropertyTypeComponent,
      ],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(PropertyTypeComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PropertyTypeComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PropertyTypeService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.propertyTypes?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to propertyTypeService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getPropertyTypeIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getPropertyTypeIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
