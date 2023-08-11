import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { OwnershipPropertyService } from '../service/ownership-property.service';

import { OwnershipPropertyComponent } from './ownership-property.component';

describe('OwnershipProperty Management Component', () => {
  let comp: OwnershipPropertyComponent;
  let fixture: ComponentFixture<OwnershipPropertyComponent>;
  let service: OwnershipPropertyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'ownership-property', component: OwnershipPropertyComponent }]),
        HttpClientTestingModule,
        OwnershipPropertyComponent,
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
      .overrideTemplate(OwnershipPropertyComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OwnershipPropertyComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(OwnershipPropertyService);

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
    expect(comp.ownershipProperties?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to ownershipPropertyService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getOwnershipPropertyIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getOwnershipPropertyIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
